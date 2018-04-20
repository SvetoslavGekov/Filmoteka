package util;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import exceptions.InvalidGenreDataException;
import exceptions.InvalidOrderDataException;
import exceptions.InvalidProductCategoryDataException;
import exceptions.InvalidProductDataException;
import exceptions.InvalidUserDataException;
import model.Movie;
import model.Product;
import model.TVSeries;
import model.dao.ProductDao;
import model.dao.nomenclatures.GenreDao;
import model.dao.nomenclatures.ProductCategoryDao;
import model.nomenclatures.Genre;
import model.nomenclatures.ProductCategory;
import util.productFilters.ProductQueryInfo;
import util.productFilters.ProductQueryInfoDeserializer;
import util.taskExecutors.CustomTaskExecutor;
import util.taskExecutors.ExpiredProductsDeleter;
import util.taskExecutors.ExpiringProductsNotifier;

public final class WebSite {
	// Fields
	private static final LocalTime TASKS_STARTING_TIME = LocalTime.now().withHour(17).withMinute(53).withSecond(00);
	private static final Map<Integer,Genre> GENRES = new TreeMap<>();
	private static final Map<Integer, ProductCategory> PRODUCT_CATEGORIES = new TreeMap<>();
	private static final List<CustomTaskExecutor> TASKS = new ArrayList<>();
	
	// Constructors --> never instantiated
	private WebSite() {

	}

	// Methods
	public static Genre getGenreById(int id) {
		return GENRES.get(id);
	}
	
	public static void addGenre(Genre g) {
		if (g != null) {
			GENRES.put(g.getId(),g);
		}
	}
	
	public static ProductCategory getProductCategoryById(int id) {
		return PRODUCT_CATEGORIES.get(id);
	}
	
	public static void addProductCategory(ProductCategory pc) {
		if (pc != null) {
			PRODUCT_CATEGORIES.put(pc.getId(),pc);
		}
	}

	public static Collection<CustomTaskExecutor> getAllTasks(){
		return Collections.unmodifiableCollection(TASKS);
	}

	public static void main(String[] args) throws SQLException, InvalidGenreDataException, InvalidProductDataException,
	InvalidUserDataException, InvalidOrderDataException, InvalidProductCategoryDataException {
		//Load all genres
		GENRES.putAll(GenreDao.getInstance().getAllGenres());
		
		//Load all product categories
		PRODUCT_CATEGORIES.putAll(ProductCategoryDao.getInstance().getAllProductCategories());
		
		//Create all utility tasks
		TASKS.add(new CustomTaskExecutor(ExpiringProductsNotifier.getInstance())); //Expiring products notifier
		TASKS.add(new CustomTaskExecutor(ExpiredProductsDeleter.getInstance()));// Expired products deleter
		
		//Start all task at the same time
		for (CustomTaskExecutor taskExecutor : TASKS) {
			taskExecutor.startExecutionAt(TASKS_STARTING_TIME.getHour(), TASKS_STARTING_TIME.getMinute(), TASKS_STARTING_TIME.getSecond());
		}
		
		//Example for a productQueryInfo deserialization
		String json = "{\r\n" + 
				"  \"name\": null,\r\n" + 
				"  \"minReleaseYear\": 1990,\r\n" + 
				"  \"maxReleaseYear\": 2005,\r\n" + 
				"  \"minDuration\": 15,\r\n" + 
				"  \"maxDuration\": 300,\r\n" + 
				"  \"minBuyCost\": 3,\r\n" + 
				"  \"maxBuyCost\": 20,\r\n" + 
				"  \"minRentCost\": 3,\r\n" + 
				"  \"maxRentCost\": 20,\r\n" + 
				"  \"genres\":[\r\n" + 
				"    1,2,3,8,10,16\r\n" + 
				"    ],\r\n" + 
				"  \"orderedBy\": \"product_id\",\r\n" + 
				"  \"isAscending\": true\r\n" + 
				"}";
		
		//Create GSON builder
		GsonBuilder gBuilder = new GsonBuilder().setPrettyPrinting();
		gBuilder.registerTypeAdapter(ProductQueryInfo.class, new ProductQueryInfoDeserializer());
		Gson gson = gBuilder.create();
		
		ProductQueryInfo pqi = gson.fromJson(json, ProductQueryInfo.class);
		System.out.println(pqi);
		
		for (Integer inti : ProductDao.getInstance().getFilteredProducts(pqi)) {
			System.out.println(inti);
		}
		
		
		//TODO --> 1) Implement a simple product factory / get product by id which calls the factory and the factory calls the first two methods.
	}
}
