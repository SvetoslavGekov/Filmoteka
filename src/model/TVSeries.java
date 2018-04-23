package model;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import exceptions.InvalidProductDataException;
import model.nomenclatures.Genre;
import model.nomenclatures.ProductCategory;

public class TVSeries extends Product {
	//Optional fields
	private Integer season;
	private LocalDate finishedAiring;
	
	//Constructors
	//Constructor for creating a new TV Series
	public TVSeries(String name, ProductCategory productCategory, LocalDate releaseDate, String pgRating,
			int duration, double rentCost, double buyCost)
			throws InvalidProductDataException {
		super(name, productCategory, releaseDate, pgRating, duration, rentCost, buyCost);
	}
	
	//Constructor for creating a new TV Series with all available information
	public TVSeries( String name, ProductCategory productCategory, LocalDate releaseDate, String pgRating, int duration, double rentCost,
			double buyCost, String description,String poster, String trailer, String writers, String actors,
			Set<Genre> genres, double salePercent, LocalDate saleValidity, Integer season, LocalDate finishedAiring) 
					throws InvalidProductDataException {
		super(name, productCategory, releaseDate, pgRating, duration, rentCost, buyCost, description,poster, trailer, writers, actors,
				genres, salePercent, saleValidity);
		setSeason(season);
		setFinishedAiring(finishedAiring);
	}
	
	//Constructor for loading a TV Series from the DB
	public TVSeries(int id, String name, ProductCategory productCategory, LocalDate releaseDate, String pgRating, int duration, double rentCost,
			double buyCost, String description,String poster, String trailer, String writers, String actors,
			Set<Genre> genres, Map<Integer,Double> raters, double salePercent, LocalDate saleValidity, Integer season, LocalDate finishedAiring)
					throws InvalidProductDataException {
		super(id, name, productCategory,releaseDate, pgRating, duration, rentCost, buyCost, description,poster, trailer, writers,
				actors, genres,raters, salePercent, saleValidity);
		setSeason(season);
		setFinishedAiring(finishedAiring);
	}
	//Methods
	@Override
	public String toString() {
		return String.format("Type:%s	Id:%d	Name:%s	Year:%s	PG:%s	Duration:%d	Rent: %.2f	Price: %.2f	%nDescr: %s%n	Post: %s	Trailer:%s"
				+ "	Writers:%s	Actors:%s	%nViewer_rating:%.2f	Season:%d	FinishedAiring:%s %nRaters: %s%n"
				+ "Sale Percent: %f	Sale Validity: %s", getProductCategory().getValue(),
				this.getId(), getName(), getReleaseDate().getYear(), getPgRating(), getDuration(), getRentCost(),getBuyCost(),
				getDescription(), getPoster(), getTrailer(), getWriters(), getActors(), getViewerRating(), getSeason(),
				getFinishedAiring(), getRaters(),getSalePercent(), getSaleValidity());
	}
	
	//Setters
	public void setSeason(Integer season) {
		if(season != null && season >= 0) {
			this.season = season;
		}
		else {
			this.season = null;
		}
	}
	
	public void setFinishedAiring(LocalDate finishedAiring) {
		this.finishedAiring = finishedAiring;
	}
	
	//Getters
	public Integer getSeason() {
		return this.season;
	}
	
	public LocalDate getFinishedAiring() {
		return this.finishedAiring;
	}
	
}
