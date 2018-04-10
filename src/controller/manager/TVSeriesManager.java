package controller.manager;

import java.sql.SQLException;
import java.time.LocalDate;

import exceptions.InvalidProductDataException;
import model.TVSeries;
import model.dao.TVSeriesDao;
import webSite.WebSite;

public class TVSeriesManager {
	//Fields
	private static TVSeriesManager instance;
	private static TVSeriesDao dao;
	
	//Constructor
	private TVSeriesManager() {
		//Instantiate the dao object
		dao = TVSeriesDao.getInstance();
	}
	
	//Methods
	public synchronized static TVSeriesManager getInstance() {
		if(instance == null) {
			instance = new TVSeriesManager();
		}
		return instance;
	}
	
	public void createNewTVSeries(String name, LocalDate releaseDate, String pgRating, int duration, double rentCost, double buyCost) {
		TVSeries tvs;
		try {
			//Create new movie with the given data
			tvs = new TVSeries(name, releaseDate, pgRating, duration, rentCost, buyCost);
			//Add movie to DB
			dao.saveTVSeries(tvs);
			//Add movie to the products collection
			WebSite.addProduct(tvs);
		}
		catch (InvalidProductDataException | SQLException e) {
			// TODO Handle movie exception
			e.printStackTrace();
		}
	}
	
	
}
