package model;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import exceptions.InvalidProductDataException;

public class TVSeries extends Product {
	//Optional fields
	private int season;
	private LocalDate finishedAiring;
	
	//Constructors
	//Constructor for creating a new movie
	public TVSeries(String name, LocalDate releaseDate, String pgRating, int duration, double rentCost, double buyCost)
			throws InvalidProductDataException {
		super(name, releaseDate, pgRating, duration, rentCost, buyCost);
	}
	
	
	//Constructor for loading a movie from the DB
	public TVSeries(int id, String name, LocalDate releaseDate, String pgRating, int duration, double rentCost,
			double buyCost, String description,String poster, String trailer, String writers, String actors,
			Set<Genre> genres, Map<Integer,Double> raters, int season, LocalDate finishedAiring) throws InvalidProductDataException {
		super(id, name, releaseDate, pgRating, duration, rentCost, buyCost, description,poster, trailer, writers, actors, genres,raters);
		setSeason(season);
		setFinishedAiring(finishedAiring);
	}
	//Methods
	@Override
	public String toString() {
		return String.format("Type:%s	Id:%d	Name:%s	Year:%s	PG:%s	Duration:%d	Rent: %.2f	Price: %.2f	%nDescr: %s%n	Post: %s	Trailer:%s"
				+ "	Writers:%s	Actors:%s	%nViewer_rating:%.2f	Season:%d	FinishedAiring:%s %nRaters: %s%n", this.getClass().getSimpleName(),
				this.getId(), getName(), getReleaseDate().getYear(), getPgRating(), getDuration(), getRentCost(),getBuyCost(),
				getDescription(), getPoster(), getTrailer(), getWriters(), getActors(), getViewerRating(), getSeason(),getFinishedAiring(), getRaters());
	}
	
	//Setters
	public void setSeason(int season) {
		if(season >= 0) {
			this.season = season;
		}
	}
	
	public void setFinishedAiring(LocalDate finishedAiring) {
		this.finishedAiring = finishedAiring;
	}
	
	//Getters
	public int getSeason() {
		return this.season;
	}
	
	public LocalDate getFinishedAiring() {
		return this.finishedAiring;
	}
}
