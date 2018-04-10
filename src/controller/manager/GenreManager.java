package controller.manager;

import java.sql.SQLException;

import exceptions.InvalidGenreDataException;
import model.Genre;
import model.dao.GenreDao;
import webSite.WebSite;

public final class GenreManager {
	//Fields
	private static GenreManager instance;
	private static GenreDao dao;
	
	//Constructor
	private GenreManager() {
		//Instantiate the dao object
		dao = GenreDao.getInstance();
	}
	
	//Methods
	public synchronized static GenreManager getInstance() {
		if(instance == null) {
			instance = new GenreManager();
		}
		return instance;
	}
	
	public void createNewGenre(String genreName) {
		Genre g;
		try {
			//Create new genre with the given data
			g = new Genre(genreName);
			//Add genre to DB
			dao.saveGenre(g);
			//Add genre to the GENRES collection
			WebSite.addGenre(g);
		}
		catch (InvalidGenreDataException | SQLException e) {
			// TODO Handle genre exception
			e.printStackTrace();
		}
	}
	
	public void updateExistingGenre(Genre g, String newGenreName) {
		try {
			//Set new genre characteristics
			g.setValue(newGenreName);
			//Update characteristics in DB
			dao.updateGenre(g);
		}
		catch (InvalidGenreDataException | SQLException e) {
			// TODO Handle genre exception
			e.printStackTrace();
		}

	}
}
