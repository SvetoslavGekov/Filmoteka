package controller.manager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import exceptions.InvalidOrderDataException;
import exceptions.InvalidProductDataException;
import exceptions.InvalidUserDataException;
import model.Order;
import model.Product;
import model.SimpleUserFactory;
import model.User;
import model.dao.UserDao;
import util.WebSite;
import validation.Supp;

public class UserManager {

	private static UserManager instance;
	private UserDao dao;

	private UserManager() {
		// Instantiate the dao object
		this.dao = UserDao.getInstance();
	}

	public static synchronized UserManager getInstance() {
		if (instance == null) {
			instance = new UserManager();
		}
		return instance;
	}

	public synchronized boolean register(String firstName, String lastName, String username, String password,
			String email) throws InvalidUserDataException, SQLException {
		User u = null;
		// Create new user with the given information
		boolean isAdmin = false;
		u = SimpleUserFactory.createUser(isAdmin, firstName, lastName, username, password, email);

		// Save user in the database
		this.dao.saveUser(u);

		// Set the users password to the salt
		u.setPassword(u.hashPassword());

		return true;
	}

	public User logIn(String username, String password) throws InvalidProductDataException {
		try {
			User u = this.dao.getUserByLoginCredentials(username, password);
			if (u != null) {
				u.setLastLogin(LocalDateTime.now());
				dao.updateUser(u);
			}
			return u;
		}
		catch (SQLException | InvalidUserDataException | InvalidOrderDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean addOrRemoveProductFromFavorites(User user, Product product) {
		// Check if user will add or remove the product
		List<Integer> favorites = new ArrayList<>(user.getFavourites());

		try {
			// If the user already has the product in his favorites
			if (Collections.binarySearch(favorites, product.getId()) >= 0) {
				// Remove product from user's favorites in the DB and in the POJO
				this.dao.removeProductFromFavorites(user, product);
				user.removeFavoriteProduct(product.getId());
			}
			// If the user doesn't have the product in his favorites
			else {
				// Add product to user's favorites in the DB and in the POJO
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

	public boolean addOrRemoveProductFromWatchlist(User user, Product product) throws SQLException {
		// Check if user will add or remove the product
		List<Integer> watchlist = new ArrayList<>(user.getWatchList());

		// If the user already has the product in his watchlist
		if (Collections.binarySearch(watchlist, product.getId()) >= 0) {
			// Remove product from user's watchlist in the DB and in the POJO
			this.dao.removeProductFromWatchlist(user, product);
			user.removeWatchlistProduct(product.getId());
		}
		// If the user doesn't have the product in his watchlist
		else {
			// Add product to user's watchlist in the DB and in the POJO
			this.dao.addProductToWatchlist(user, product);
			user.addWatchlistProduct(product.getId());
		}

		return true;
	}

	public void addProductToShoppingCart(User user, Product product, boolean willBuy) {
		// Check if user does not own the product
		if (!user.ownsProduct(product)) {
			user.addProductToCart(product, willBuy);
		}
	}

	public void removeProductFromShoppingCart(User user, Product product) {
		user.removeProductFromCart(product);
	}

	public void buyProductsInCart(User user) throws SQLException, InvalidOrderDataException {
		Map<Product, LocalDate> shoppingCart = user.getShoppingCart();
		// If there is nothing to be bought
		if (shoppingCart.isEmpty()) {
			return;
		}

		// Check if user has enough money
		double cartPrice = user.getShoppingCartPrice();
		double userMoney = user.getMoney();
		if (cartPrice <= userMoney) {
			// Create new order and add it to the DB and user's collection
			Order order = OrderManager.getInstance().createNewOrder(user, LocalDate.now(), shoppingCart);
			user.addOrder(order);

			// Transfer money
			user.setMoney(userMoney - cartPrice);
			// Add products to user's collection
			user.addProductsFromShoppingCart();
			// Update user and his products in db
			dao.updateUser(user);
			dao.saveUserProductsInCartById(user.getUserId(), shoppingCart);
			// Clear shopping cart
			user.cleanCart();
		}
		else {
			// TODO --> throw exception or tell user he has no money
		}
	}

	public boolean isValidUserRegistrationData(String username, String password, String email) {
		if (!(Supp.isValidUsername(username) && Supp.isValidEmail(email) && Supp.isValidPassword(password))) {
			return false;
		}
		return true;
	}

	public boolean hasUserWithSameCredentials(String username, String email) throws SQLException {
		return dao.databaseHasUserWithCredentials(username, email);
	}
}
