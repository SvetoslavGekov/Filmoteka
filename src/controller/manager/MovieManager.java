package controller.manager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Set;

import exceptions.InvalidProductDataException;
import model.Movie;
import model.dao.MovieDao;
import model.nomenclatures.Genre;
import model.nomenclatures.ProductCategory;
import util.WebSite;

public class MovieManager {
	// Fields
	private static MovieManager instance;
	private MovieDao dao;
	
	// Constructor
	private MovieManager() {
		// Instantiate the dao object
		dao = MovieDao.getInstance();
	}

	// Methods
	public synchronized static MovieManager getInstance() {
		if (instance == null) {
			instance = new MovieManager();
		}
		return instance;
	}
	
	public synchronized void updateMovie(int originalID, String name, int categoryId, LocalDate releaseDate, String pgRating, int duration,
			double rentCost, double buyCost, String description, String poster, String trailer, String writers,
			String actors, Set<Genre> genres, double salePercent, LocalDate saleValidity, String director ) 
					throws InvalidProductDataException, SQLException {
		
		//Create the category
		ProductCategory productCategory = WebSite.getProductCategoryById(categoryId);
		
		//Set the movie's fields
		Movie m = new Movie(name, productCategory,releaseDate, pgRating, duration, rentCost, buyCost, description, poster,
				trailer, writers, actors, genres, salePercent, saleValidity, director);
		m.setId(originalID);
		
		//Update the move in the database
		dao.updateMovie(m);
	}

	public synchronized void createNewMovie(String name, int categoryId, LocalDate releaseDate, String pgRating, int duration,
			double rentCost, double buyCost, String description, String poster, String trailer, String writers,
			String actors, Set<Genre> genres, double salePercent, LocalDate saleValidity, String director)
					throws InvalidProductDataException, SQLException {
		Movie m;
		
		//Create the category
		ProductCategory productCategory = WebSite.getProductCategoryById(categoryId);
		
		// Create new movie with the given data
		m = new Movie(name, productCategory, releaseDate, pgRating, duration, rentCost, buyCost, description, poster, trailer,
				writers, actors, genres, salePercent, saleValidity, director);
		
		// Add movie to DB
		dao.saveMovie(m);
		
	}

}
