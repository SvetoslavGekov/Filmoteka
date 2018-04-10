package products;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import customexceptions.InvalidProductInfoException;
import user.IUser;
import validation.Supp;

public abstract class Product implements Comparable<Product>{
	//Enumerations
	public enum ProductType{
		MOVIE, TVSERIES;
	}
	
	public enum Genre{
		ACTION, ADVENTURE, ANIMATION, BIOGRAPHY, COMEDY, CRIME,
		DOCUMENTARY, DRAMA, FAMILY, FANTASY, HISTORY, HORROR,
		MUSIC, MUSICAL, MYSTERY, ROMANCE, SCIFI, SPORT,
		SUPERHERO, THRILLER, WAR, WESTERN;
	}
	
	//Fields
	private static final double MAX_RATING = 10.0d; //Maximum rating that can be given to the product 
	public static final int RENT_DURATION = 7;
	private static int currentID = 1;
	
	private int id;
	private String name;
	private String description = "";
	private ProductType type;
	private String trailer = ""; //Link to trailer
	private String writers = "";
	private String actors = "";
	private LocalDate releaseDate;
	private double viewerRating;
	private int totalVotes; 
	private String pgRating;
	private double sumOfUsersRatings;
	private int duration; //Product duration in minutes
	private Set<Genre> genres = new HashSet<>();
	private Map<IUser, Double> raters = new HashMap<>();
	private double rentCost;
	private double buyCost;
	
	//Constructors
	public Product(String name, ProductType type, LocalDate releaseDate, String pgRating, int duration,
			double rentCost, double buyCost) {
		setId();
		setName(name);
		setType(type);
		setReleaseDate(releaseDate);
		setPgRating(pgRating);
		setDuration(duration);
		setRentCost(rentCost);
		setBuyCost(buyCost);
	}
	
	//Methods
	
	//==================== MENU PRINTING AND SELECTION METHODS =====================
	protected abstract boolean selectFromAdvancedEditingMenu(int option);
	
	public boolean selectFromProductEditingMenu() {
		System.out.print("\nPlease enter one of the product editing options to continue: ");
		
		//Get input for the chosen option
		int option = Supp.getPositiveNumber();
		switch (option) {
			case 1: this.printFullInfo(); return true;
			case 2: this.editName(); return true; 
			case 3: this.editReleaseDate(); return true;
			case 4: this.editPGRating(); return true;
			case 5: this.editDuration(); return true;
			case 6: this.editRentCost(); return true;
			case 7: this.editBuyCost(); return true;
			case 8: this.editGenres(); return true;
			case 9: this.editDescription(); return true;
			case 10: this.editTrailer(); return true;
			case 11: this.editWriters(); return true;
			case 12: this.editActors(); return true;
			case 99: return false;
		default: 
			 return (selectFromAdvancedEditingMenu(option)) ? false : true;
		}
	}
	
	public void printProductEditingMenu() {
		System.out.println("\n=============== PRODUCT EDITING MENU =================");
		System.out.println(this);
		System.out.println();
		System.out.println("	99) Back to main menu");
		System.out.println("	1) View full info");
		System.out.println("	2) Edit name");
		System.out.println("	3) Edit release date");
		System.out.println("	4) Edit PG Rating");
		System.out.println("	5) Edit duration");
		System.out.println("	6) Edit rent cost");
		System.out.println("	7) Edit price");
		System.out.println("	8) Edit genres");
		System.out.println("	9) Edit description");
		System.out.println("	10) Edit trailer");
		System.out.println("	11) Edit writers");
		System.out.println("	12) Edit actors");
	}
	
	
	//=================== PRODUCT EDITING METHODS ================
	private void editName() {
		System.out.println("\nCurrent name: " + this.name);
		this.setName(Product.inputProductName());
	}
	
	private void editReleaseDate() {
		System.out.println("\nCurrent release date: " + this.releaseDate);
		try {
			this.setReleaseDate(Product.inputProductReleaseDate());
		}
		catch (InvalidProductInfoException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void editPGRating() {
		System.out.println("\nCurrent PG Rating: " + this.pgRating);
		try {
			this.setPgRating(Product.inputPGRating());
		}
		catch (InvalidProductInfoException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void editDuration() {
		System.out.println("\nCurrent duration: " + this.duration);
		try {
			this.setDuration(Product.inputDuration());
		}
		catch (InvalidProductInfoException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void editRentCost() {
		System.out.println("\nCurrent rent cost: " + this.rentCost);
		try {
			this.setRentCost(Product.inputRentCost());
		}
		catch (InvalidProductInfoException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void editBuyCost() {
		System.out.println("\nCurrent price is: " + this.buyCost);
		try {
			this.setBuyCost(Product.inputBuyCost());
		}
		catch (InvalidProductInfoException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void editGenres() {
		System.out.println("\nCurrent genres are: " + this.genres);
		
		try {
			//Ask user to add or remove genres
			System.out.println("Would you like to:\n	1) Add a genre;\n	2) Remove a genre;");
			System.out.print("Please enter an option to continue: ");
			int option = Supp.getPositiveNumber();
			
			switch (option) {
			case 1:	this.addGenre(Product.inputGenre()); break;
			case 2: this.removeGenre(Product.inputGenre()); break;
			default:
				System.out.println("Invalid option selected.");
				this.printProductEditingMenu();
				break;
			}
		}
		catch (InvalidProductInfoException e) {
			System.out.println(e.getMessage());
			Product.printGenres();
		}
	}
	
	private void editDescription() {
		System.out.println("\nCurrent description: " + this.description);
		this.setDescription(Product.inputProductDescription());
	}
	
	private void editTrailer() {
		System.out.println("\nCurrent trailer link: " + this.trailer);
		this.setTrailer(Product.inputProductTrailer());
	}
	
	private void editWriters() {
		System.out.println("\nCurrent writers: " + this.writers);
		this.setWriters(Product.inputProductWriters());
	}
	
	private void editActors() {
		System.out.println("\nCurrent actors: " + this.actors);
		this.setActors(Product.inputProductActors());
	}
	
	
	@Override
	public int compareTo(Product o) {
		return this.id - o.id;
	}
	
	public void printFullInfo() {
		System.out.println("======= PRODUCT INFORMATION ======");
		System.out.printf("Identification number: %d%n", this.id);
		System.out.printf("Name: %s%n", this.name);
		System.out.printf("Description: %s%n", this.description);
		System.out.printf("Type: %s%n", this.type);
		System.out.printf("Trailer link: %s%n", this.trailer);
		System.out.printf("Writers: %s%n", this.writers);
		System.out.printf("Actors: %s%n", this.actors);
		System.out.printf("Viewer rating: %.2f (Total votes: %d)%n", this.viewerRating, this.totalVotes);
		System.out.printf("PG Rating: %s%n", this.pgRating);
		System.out.printf("Duration: %d%n", this.duration);
		System.out.printf("Genres: %s%n", this.genres);
		System.out.printf("Rent cost: %.2f%n", this.rentCost);
		System.out.printf("Price: %.2f%n", this.buyCost);
	}
	
	@Override
	//Acts as simple information for the product
	public String toString() {
		return String.format("Name: %s	ID: %d	Type: %s	Release date: %s	PGRating: %s	Duration: %s	RentCost: %.2f	Price: %.2f", 
				this.name, this.id, this.type, this.releaseDate, this.pgRating, this.duration, this.rentCost, this.buyCost);
	}
	
	//================ PRODUCT CONSOLE INPUT METHODS ================
	
	private static Genre inputGenre() throws InvalidProductInfoException{
		//User inputs genre
		System.out.print("Please enter a desired genre: ");
		String input = Supp.inputValidString();
		Genre genre = getGenreByName(input);
		if(genre == null) {
			throw new InvalidProductInfoException("You've entered an invalid genre name.");
		}
		return genre;
	}
	
	public static String inputProductName(){
		//User inputs product name
		System.out.print("Please enter the product name: ");
		String name = Supp.inputValidString();
		
		return name;
	}
	
	public static int inputProductType() throws InvalidProductInfoException{
		ProductType[] allProducts = ProductType.values();
		System.out.println("\nAvailable product types:");
		int counter = 1;
		for (ProductType pType : allProducts) {
			System.out.printf("%d) %s;%n",counter++, pType);
		}
		
		System.out.print("Choose a product type to continue: ");
		int pType = Supp.getPositiveNumber();
		if(pType <= 0 || pType > allProducts.length) {
			throw new InvalidProductInfoException("You've entered an invalid product type.");
		}
		return pType;
	}
	
	public static final String inputProductActors() {
		//User inputs actors (can be empty)
		System.out.print("Please enter the product's actors: ");
		return Supp.inputString();
	}
	
	public static final String inputProductWriters() {
		//User inputs writers (can be empty)
		System.out.print("Please enter the product's writers: ");
		return Supp.inputString();
	}
	
	public static final String inputProductTrailer() {
		//User inputs trailer link (can be empty)
		System.out.print("Please enter the product's trailer: ");
		return Supp.inputString();
	}
	
	public static String inputProductDescription() {
		//User inputs description (can be empty)
		System.out.print("Please enter the product's description: ");
		return Supp.inputString();
	}
	
	public static int inputProductId(){
		//User inputs product id
		System.out.print("Please enter the product's id: ");
		int id = Supp.getPositiveNumber();
		return id;
	}
	
	public static double inputBuyCost() throws InvalidProductInfoException{
		//User inputs product cost
		System.out.print("Please enter the product's price: ");
		double buyCost = Supp.getDouble();
		if(buyCost <= 0d) {
			throw new InvalidProductInfoException("You've entered an invalid price. Prices should be positive numbers.");
		}
		return buyCost;
	}
	
	public static double inputRentCost() throws InvalidProductInfoException{
		//User inputs product rent cost
		System.out.print("Please enter the product's rent cost: ");
		double rentCost = Supp.getDouble();
		if(rentCost <= 0d) {
			throw new InvalidProductInfoException("You've entered an invalid rent cost. Rent costs should be positive numbers.");
		}
		return rentCost;
	}
	
	public static int inputDuration() throws InvalidProductInfoException{
		//User inputs product duration
		System.out.print("Please enter the product duration: ");
		int duration = Supp.getPositiveNumber();
		if(duration <= 0) {
			throw new InvalidProductInfoException("You've entered an invalid duration. Product durations should be positive numbers");
		}
		return duration;
	}
	
	public static String inputPGRating() throws InvalidProductInfoException {
		//User inputs product pgRating
		System.out.print("Please enter the product pgRating: ");
		String pgRating = Supp.inputValidString();
		if(!Supp.validStr(pgRating)) {
			throw new InvalidProductInfoException("Sorry, you've entered an invalid string for the PG Rating.");
		}
		return pgRating;
	}
	
	public static LocalDate inputProductReleaseDate() throws InvalidProductInfoException{
		//User inputs product release date
		System.out.print("Please enter the product release date: ");
		String input = Supp.inputValidString();
		LocalDate releaseDate = null;
		try {
			releaseDate = LocalDate.parse(input);
		}
		catch (DateTimeParseException e) {
			throw new InvalidProductInfoException("You've entered an invalid date.\nDates should have the following format - \"yyyy-mm-dd\"");
		}
		return releaseDate;
	}
	
	//=============== MENU SELECTION METHODS =================
	
	public boolean rate(double rate, IUser user) {
		if(rate > Product.MAX_RATING){
			return false;
		}
		if(!this.raters.containsKey(user)){
			this.raters.put(user, rate);
			setTotalVotes(this.totalVotes++);
		}
		this.raters.replace(user, rate);
		this.updateRating();
		return true;
	}
	
	private void updateRating(){
		double allRate = 0;
		for (Double rate : this.raters.values()) {
			allRate += rate;
		}
		setViewerRating(allRate/this.totalVotes);
	}
	
	private static final Genre getGenreByName(String genreName) {
		Genre[] genres = Product.Genre.values();
		for (Genre genre : genres) {
			if(genre.toString().equalsIgnoreCase(genreName)) {
				return genre;
			}
		}
		return null;
	}
	
	private static final void printGenres() {
		System.out.println("The available genres are: ");
		System.out.println(Arrays.toString(Product.Genre.values()));
	}
	
	//Setters
	private static void setCurrentID(int currentID) {
		Product.currentID = currentID;
	}
	
	private void removeGenre(Genre genre) {
		if(genre != null) {
			this.genres.remove(genre);
		}
	}
	
	private void addGenre(Genre genre) {
		if(genre != null) {
			this.genres.add(genre);
		}
	}
	
	private void setId() {
		this.id = Product.currentID++;
	}

	private void setName(String name) {
		if(Supp.validStr(name)) {
			this.name = name;
		}
	}

	private void setDescription(String description) {
		if(description != null) {
			this.description = description;
		}
	}

	private void setType(ProductType type) {
		if(type != null) {
			this.type = type;
		}
	}

	private void setTrailer(String trailer) {
		if(trailer != null) {
			this.trailer = trailer;
		}
	}

	private void setWriters(String writers) {
		if(writers != null) {
			this.writers = writers;
		}
	}

	private void setActors(String actors) {
		if(actors != null) {
			this.actors = actors;
		}
	}

	private void setReleaseDate(LocalDate releaseDate) {
		if(releaseDate != null) {
			this.releaseDate = releaseDate;
		}
	}

	private void setViewerRating(double viewerRating) {
		if( viewerRating >= 0 && viewerRating < Product.MAX_RATING){
			this.viewerRating = viewerRating;
		}
	}

	private void setTotalVotes(int totalVotes) {
		if(totalVotes > 0) {
			this.totalVotes = totalVotes;
		}
	}

	private void setPgRating(String pgRating) {
		if(Supp.validStr(pgRating)) {
			this.pgRating = pgRating;
		}
	}

	private void setDuration(int duration) {
		if(duration > 0) {
			this.duration = duration;
		}
	}

	private void setRentCost(double rentCost) {
		if(rentCost > 0) {
			this.rentCost = rentCost;	
		}
	}

	private void setBuyCost(double buyCost){
		if(buyCost > 0) {
			this.buyCost = buyCost;
		}
	}
	
	//Getters
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public double getRentCost() {
		return this.rentCost;
	}
	
	public double getBuyCost() {
		return this.buyCost;
	}
	
	//Comparators
	public static final Comparator<Product> compareByNameAndReleaseDate = new Comparator<Product>() {
		public int compare(Product o1, Product o2) {
			//If they have same names
			if(o1.name.equalsIgnoreCase(o2.name)) {
				//If they have same releaseDates
				if(o1.releaseDate.isEqual(o2.releaseDate)) {
					return 0;
				}
				//Return date comparison
				return o1.releaseDate.compareTo(o2.releaseDate);
			}
			else {
				//If they don't have same names --> return comparison by names
				return o1.name.compareTo(o2.name);
			}
		};
	};
	
}
