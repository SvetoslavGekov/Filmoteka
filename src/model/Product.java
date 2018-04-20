package model;



import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import exceptions.InvalidProductDataException;
import model.nomenclatures.Genre;
import validation.Supp;

public abstract class Product implements Comparable<Product>{
	//Mandatory fields
	private static final double BASE_PERCENT = 100d;
	private int id;
	private String name;
	private LocalDate releaseDate;
	private String pgRating;
	private int duration; //Product duration in minutes
	private final double originalRentCost;
	private final double originalBuyCost;
	private double rentCost;
	private double buyCost;
	private double salePercent;
	private LocalDate saleValidity;
	
	//Optional fields
	private String description;
	private String poster; //Link to poster
	private String trailer; //Link to trailer
	private String writers;
	private String actors;
	private double viewerRating;
	private Set<Genre> genres = new HashSet<>();
	private Map<Integer, Double> raters = new TreeMap<Integer,Double>();//Key: UserId -> Value: User rating


	//Constructors
	//Constructor for creating a new product with the basic information
	public Product(String name, LocalDate releaseDate, String pgRating, int duration, double rentCost, double buyCost) 
			throws InvalidProductDataException {
		setName(name);
		setReleaseDate(releaseDate);
		setPgRating(pgRating);
		setDuration(duration);
		
		//Setting the costs according to the product being on sale or not (validation is made in the setters);
		this.originalBuyCost = buyCost;
		this.originalRentCost = rentCost;
		setRentCost(rentCost);
		setBuyCost(buyCost);
	}
	
	//Constructor for creating a new product with all available information
	public Product(String name, LocalDate releaseDate, String pgRating, int duration, double rentCost,
			double buyCost, String description, String poster, String trailer, String writers, String actors,
			Set<Genre> genres,double salePercent, LocalDate saleValidity) throws InvalidProductDataException {
		this(name, releaseDate, pgRating, duration, rentCost, buyCost);
		//Skipping id + raters + calculation of rating
		setDescription(description);
		setPoster(poster);
		setTrailer(trailer);
		setWriters(writers);
		setActors(actors);
		setGenres(genres);
		
		//Setting the costs according to the product being on sale or not (validation is made in the setters);
		setSalePercent(salePercent);
		setSaleValidity(saleValidity);
		setRentCost(isOnSale() ? (getOriginalRentCost()*(BASE_PERCENT-getSalePercent())/BASE_PERCENT) : getOriginalRentCost());
		setBuyCost(isOnSale() ? (getOriginalBuyCost()*(BASE_PERCENT-getSalePercent())/BASE_PERCENT) : getOriginalBuyCost());
	}

	//Constructor for loading a product from the DB
	public Product(int id, String name, LocalDate releaseDate, String pgRating, int duration, double rentCost,
			double buyCost, String description, String poster, String trailer, String writers, String actors,
			Set<Genre> genres, Map<Integer, Double> raters, double salePercent, LocalDate saleValidity) throws InvalidProductDataException {
		this(name, releaseDate, pgRating, duration, rentCost, buyCost, description, poster, trailer, writers, actors,
				genres, salePercent, saleValidity);
		setId(id);

		//Calculate and set the viewerRating
		setViewerRating(calculateRating());
	}



	//Setters
	public double calculateRating() {
		double totalVotes = (double) this.raters.size();
		
		if(totalVotes < 1) {
			return 0.0d;
		}
		
		double sumOfRatings = 0d;
		for (Double rating : this.raters.values()) {
			sumOfRatings += rating;
		}
		return (sumOfRatings/totalVotes);
	}
	
	public void setId(int id) throws InvalidProductDataException {
		if(id > 0) {
			this.id = id;
		}
		else {
			throw new InvalidProductDataException("Invalid product id.");
		}
	}

	public void setName(String name) throws InvalidProductDataException {
		if(Supp.isValidStr(name)) {
			this.name = name;
		}
		else {
			throw new InvalidProductDataException("Invalid product name.");
		}
	}

	public void setReleaseDate(LocalDate releaseDate) throws InvalidProductDataException {
		if(releaseDate != null) {
			this.releaseDate = releaseDate;
		}
		else {
			throw new InvalidProductDataException("Invalid product release date.");
		}
	}

	public void setPgRating(String pgRating) throws InvalidProductDataException {
		if(Supp.isValidStr(pgRating)) {
			this.pgRating = pgRating;
		}
		else {
			throw new InvalidProductDataException("Invalid product PG Rating");
		}
	}

	public void setDuration(int duration) throws InvalidProductDataException {
		if(duration > 0) {
			this.duration = duration;
		}
		else {
			throw new InvalidProductDataException("Invalid product duration");
		}
	}

	public void setRentCost(double rentCost) throws InvalidProductDataException {
		if(rentCost > 0) {
			this.rentCost = rentCost;
		}
		else {
			throw new InvalidProductDataException("Invalid product rent cost");
		}
	}

	public void setBuyCost(double buyCost) throws InvalidProductDataException {
		if(buyCost > 0) {
			this.buyCost = buyCost;
		}
		else {
			throw new InvalidProductDataException("Invalid product buying price");
		}
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}
	
	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}

	public void setWriters(String writers) {
		this.writers = writers;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public void setViewerRating(double viewerRating) {
		if(viewerRating >= 0) {
			this.viewerRating = viewerRating;
		}
	}

	public void setGenres(Set<Genre> genres) {
		this.genres = genres;
	}

	public void setRaters(Map<Integer, Double> raters) {
		if(raters != null) {
			this.raters = raters;
		}
	}
	
	public void addRater(User user, double rate) {
		//If the user retes the product for first time
		if(!this.raters.containsKey(user)){
			// Add new rater with his rating
			this.raters.put(user.getUserId(), rate);
			return;
		}
		// Update his rating
		this.raters.replace(user.getUserId(), rate);
	}
	
	public void setSalePercent(double salePercent) {
		if(salePercent >= 0d && salePercent < 100d) {
			this.salePercent = salePercent;
		}
	}
	
	public void setSaleValidity(LocalDate saleValidity) {
		this.saleValidity = saleValidity;
	}
	
	private boolean isOnSale() {
		if(this.saleValidity != null) {
			return !LocalDate.now().isAfter(this.saleValidity);
		}
		return false;
	}
	
	//Getters
	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public LocalDate getReleaseDate() {
		return this.releaseDate;
	}

	public String getPgRating() {
		return this.pgRating;
	}

	public int getDuration() {
		return this.duration;
	}

	public double getRentCost() {
		return this.rentCost;
	}

	public double getBuyCost() {
		return this.buyCost;
	}

	public String getDescription() {
		return this.description;
	}

	public String getPoster() {
		return this.poster;
	}

	public String getTrailer() {
		return this.trailer;
	}

	public String getWriters() {
		return this.writers;
	}

	public String getActors() {
		return this.actors;
	}

	public double getViewerRating() {
		return this.viewerRating;
	}

	public Set<Genre> getGenres() {
		return Collections.unmodifiableSet(this.genres);
	}
	
	public Map<Integer, Double> getRaters() {
		return Collections.unmodifiableMap(this.raters);
	}
	
	public double getSalePercent() {
		return this.salePercent;
	}
	
	public LocalDate getSaleValidity() {
		return this.saleValidity;
	}
	
	public double getOriginalBuyCost() {
		return this.originalBuyCost;
	}
	
	public double getOriginalRentCost() {
		return this.originalRentCost;
	}
	
	@Override
	public int compareTo(Product o) {
		return this.getId() - o.getId();
	}
}
