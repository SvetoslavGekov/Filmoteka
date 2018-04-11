package controller.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exceptions.InvalidUserDataException;
import model.Product;
import model.User;
import model.dao.UserDao;
import webSite.WebSite;

public class UserManager {

	private static UserManager instance;
	private UserDao dao;
	
	private UserManager() {
		//Instantiate the dao object
		this.dao = UserDao.getInstance();
	}

	public static synchronized UserManager getInstance() {
		if (instance == null) {
			instance = new UserManager();
		}
		return instance;
	}

	public boolean register(String firstName, String lastName, String username, String password, String email) {
		User u = null;
		try {
			//Create new user with the given information
			u = new User(firstName, lastName, username, password, email);
			//Save user in the databse
			this.dao.saveUser(u);
			//Add user in the users collection
			WebSite.addUser(u);
			return true;
		}
		catch (SQLException | InvalidUserDataException e) {
			//TODO handle exception
			return false;
		}
	}

	public User logIn(String username, String password) {
		try {
			return this.dao.getUserByLoginCredentials(username, password);
		}
		catch (SQLException | InvalidUserDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean addOrRemoveProductFromFavorites(User user, Product product) {
		//Check if user will add or remove the product
		List<Integer> favorites = new ArrayList<>(user.getFavourites());
		
		try {
			//If the user already has the product in his favorites
			if(Collections.binarySearch(favorites, product.getId()) >= 0) {
				//Remove product from user's favorites in the DB and in the POJO
				this.dao.removeProductFromFavorites(user, product);
				user.removeFavoriteProduct(product.getId());
			}
			//If the user doesn't have the product in his favorites
			else {
				//Add product to user's favorites in the DB and in the POJO
				this.dao.addProductToFavorites(user, product);
				user.addFavoriteProduct(product.getId());
			}
		}
		catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean addOrRemoveProductFromWatchlist(User user, Product product) {
		//Check if user will add or remove the product
		List<Integer> watchlist = new ArrayList<>(user.getWatchList());
		
		try {
			//If the user already has the product in his watchlist
			if(Collections.binarySearch(watchlist, product.getId()) >= 0) {
				//Remove product from user's watchlist in the DB and in the POJO
				this.dao.removeProductFromWatchlist(user, product);
				user.removeWatchlistProduct(product.getId());
			}
			//If the user doesn't have the product in his watchlist
			else {
				//Add product to user's watchlist in the DB and in the POJO
				this.dao.addProductToWatchlist(user, product);
				user.addWatchlistProduct(product.getId());
			}
		}
		catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void addProductToShoppingCart(User user, Product product, boolean willBuy) {
		user.addProductToCart(product, willBuy);
	}

	public void removeProductFromShoppingCart(User user, Product product) {
		user.removeProductFromCart(product);
	}
	
	public void buyProductsInCart(User user) {
		//If there is nothing to be bought
		if(user.getShoppingCart().isEmpty()) {
			return;
		}
		
		
	}
}
