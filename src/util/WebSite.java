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

import exceptions.InvalidGenreDataException;
import exceptions.InvalidOrderDataException;
import exceptions.InvalidProductDataException;
import exceptions.InvalidUserDataException;
import model.Genre;
import model.Movie;
import model.Product;
import model.TVSeries;
import model.User;
import model.dao.GenreDao;
import model.dao.ProductDao;
import util.taskExecutors.CustomTaskExecutor;
import util.taskExecutors.ExpiredProductsDeleter;
import util.taskExecutors.ExpiringProductsNotifier;

public final class WebSite {
	// Fields
	private static final LocalTime TASKS_STARTING_TIME = LocalTime.now().withHour(19).withMinute(22).withSecond(10);
	private static final Map<Integer,Genre> GENRES = new TreeMap<>();
	private static final Map<Integer,Product> PRODUCTS = new ConcurrentHashMap<>(); 
	private static final Map<Integer,Movie> MOVIES = new ConcurrentHashMap<>(); 
	private static final Map<Integer,TVSeries> TVSERIES = new ConcurrentHashMap<>(); 
	private static final Map<Integer,User> USERS = new ConcurrentHashMap<>();
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
	
	public static void addProduct(Product p) {
		if(p != null) {
			PRODUCTS.put(p.getId(), p);
		}
	}
	
	public static void addUser(User u) {
		if(u != null) {
			USERS.put(u.getUserId(), u);
		}
	}
	
	public static User getUserByID(int userId) {
		return USERS.get(userId);
	}
	
	public static Product getProductById(int productId) {
		return PRODUCTS.get(productId);
	}
	
	public static Product getMovieById(int movieId) {
		return MOVIES.get(movieId);
	}
	
	public static Product getTVSerieById(int tvSerieId) {
		return TVSERIES.get(tvSerieId);
	}
	
	public static Collection<Product> getAllProducts() {
		return Collections.unmodifiableCollection(PRODUCTS.values());
	}
	
	public static Collection<Product> getAllMovies() {
		return Collections.unmodifiableCollection(MOVIES.values());
	}
	
	public static Collection<Product> getAllTVSeries() {
		return Collections.unmodifiableCollection(TVSERIES.values());
	}
	
	public static Collection<CustomTaskExecutor> getAllTasks(){
		return Collections.unmodifiableCollection(TASKS);
	}

	public static void main(String[] args) throws SQLException, InvalidGenreDataException, InvalidProductDataException, InvalidUserDataException, InvalidOrderDataException {
		//Load all genres
		GENRES.putAll(GenreDao.getInstance().getAllGenres());
		
		//Load all products
		for (Product p : ProductDao.getInstance().getAllProducts()) {
			if(p instanceof Movie){
				MOVIES.put(p.getId(), (Movie) p);
			}
			if(p instanceof TVSeries){
				TVSERIES.put(p.getId(), (TVSeries) p);
			}
			PRODUCTS.put(p.getId(), p);
		}
		
		//Create all utility tasks
//		TASKS.add(new CustomTaskExecutor(ExpiringProductsNotifier.getInstance())); //Expiring products notifier
//		TASKS.add(new CustomTaskExecutor(ExpiredProductsDeleter.getInstance()));// Expired products deleter
//		
//		//Start all task at the same time
//		for (CustomTaskExecutor taskExecutor : TASKS) {
//			taskExecutor.startExecutionAt(TASKS_STARTING_TIME.getHour(), TASKS_STARTING_TIME.getMinute(), TASKS_STARTING_TIME.getSecond());
//		}
	}






}