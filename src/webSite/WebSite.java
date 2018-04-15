package webSite;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
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
import model.dao.MovieDao;
import model.dao.TVSeriesDao;
import model.dao.UserDao;

public final class WebSite {
	// Fields
	private static WebSite instance;
	private static final Map<Integer,Genre> GENRES = new TreeMap<>();
	private static final Map<Integer,Product> PRODUCTS = new ConcurrentHashMap<>(); 
	private static final Map<Integer,Movie> MOVIES = new ConcurrentHashMap<>(); 
	private static final Map<Integer,TVSeries> TVSERIES = new ConcurrentHashMap<>(); 
	private static final Map<Integer,User> USERS = new ConcurrentHashMap<>(); 
	// Constructors
	private WebSite() {

	}

	// Methods
	public synchronized static WebSite getInstance() {
		if (instance == null) {
			instance = new WebSite();
		}
		return instance;
	}
	
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

	public static void main(String[] args) throws SQLException, InvalidGenreDataException, InvalidProductDataException, InvalidUserDataException, InvalidOrderDataException {
		GENRES.putAll(GenreDao.getInstance().getAllGenres());
				
		for (Movie m : MovieDao.getInstance().getAllMovies()) {
			PRODUCTS.put(m.getId(), m);
			MOVIES.put(m.getId(), m);
		}
		
		for (TVSeries tvs : TVSeriesDao.getInstance().getAllTVSeries()) {
			PRODUCTS.put(tvs.getId(), tvs);
			TVSERIES.put(tvs.getId(), tvs);
		}
		
		for (User user : UserDao.getInstance().getAllUsers()) {
			USERS.put(user.getUserId(), user);
		}

//		Movie m = new Movie("Jikus", LocalDate.now(), "asd", 123, 123, 322);
//		MovieDao.getInstance().saveMovie(m);
//		m.setActors("Bace pepi");
//		m.setTrailer("wwwwwwwwww");
//		m.setDirector("Cameron John");
//		MovieDao.getInstance().updateMovie(m);
	}






}
