package controller.manager;

import java.sql.SQLException;
import java.time.LocalDate;

import exceptions.InvalidGenreDataException;
import exceptions.InvalidProductDataException;
import model.Genre;
import model.Movie;
import model.dao.MovieDao;
import webSite.WebSite;

public class MovieManager {
	//Fields
	private static MovieManager instance;
	private static MovieDao dao;
	
	//Constructor
	private MovieManager() {
		//Instantiate the dao object
		dao = MovieDao.getInstance();
	}
	
	//Methods
	public synchronized static MovieManager getInstance() {
		if(instance == null) {
			instance = new MovieManager();
		}
		return instance;
	}
	
	public void createNewMovie(String name, LocalDate releaseDate, String pgRating, int duration, double rentCost, double buyCost) {
		Movie m;
		try {
			//Create new movie with the given data
			m = new Movie(name, releaseDate, pgRating, duration, rentCost, buyCost);
			//Add movie to DB
			dao.saveMovie(m);
			//Add movie to the products collection
			WebSite.addProduct(m);
		}
		catch (InvalidProductDataException | SQLException e) {
			// TODO Handle movie exception
			e.printStackTrace();
		}
	}
	
	
}
