package controller.manager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Set;

import exceptions.InvalidProductDataException;
import model.TVSeries;
import model.dao.TVSeriesDao;
import model.nomenclatures.Genre;
import model.nomenclatures.ProductCategory;
import util.WebSite;

public class TVSeriesManager {
	// Fields
	private static TVSeriesManager instance;
	private TVSeriesDao dao;

	// Constructor
	private TVSeriesManager() {
		// Instantiate the dao object
		dao = TVSeriesDao.getInstance();
	}

	// Methods
	public synchronized static TVSeriesManager getInstance() {
		if (instance == null) {
			instance = new TVSeriesManager();
		}
		return instance;
	}

	public void createNewTVSeries( String name, int categoryId, LocalDate releaseDate, String pgRating, int duration, double rentCost,
			double buyCost, String description,String poster, String trailer, String writers, String actors,
			Set<Genre> genres, double salePercent, LocalDate saleValidity, int season, LocalDate finishedAiring) 
					throws InvalidProductDataException, SQLException {
		TVSeries tvs;
		
		//Create the category
		ProductCategory productCategory = WebSite.getProductCategoryById(categoryId);
		
		// Create new movie with the given data
		tvs = new TVSeries(name, productCategory, releaseDate, pgRating, duration, rentCost, buyCost, description, poster, trailer,
				writers, actors, genres, salePercent, saleValidity, season, finishedAiring);
		// Add movie to DB
		dao.saveTVSeries(tvs);

	}

}
