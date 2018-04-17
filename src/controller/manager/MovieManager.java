package controller.manager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Set;

import exceptions.InvalidProductDataException;
import model.Genre;
import model.Movie;
import model.dao.MovieDao;
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

	public synchronized void createNewMovie(String name, LocalDate releaseDate, String pgRating, int duration,
			double rentCost, double buyCost, String description, String poster, String trailer, String writers,
			String actors, Set<Genre> genres, double salePercent, LocalDate saleValidity, String director)
					throws InvalidProductDataException, SQLException {
		Movie m;
		// Create new movie with the given data
		m = new Movie(name, releaseDate, pgRating, duration, rentCost, buyCost, description, poster, trailer,
				writers, actors, genres, salePercent, saleValidity, director);
		
		// Add movie to DB
		dao.saveMovie(m);
		
		// Add movie to the products collection
		WebSite.addProduct(m);
	}

}
