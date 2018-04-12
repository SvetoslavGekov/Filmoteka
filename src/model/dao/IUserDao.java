package model.dao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import exceptions.InvalidOrderDataException;
import exceptions.InvalidUserDataException;
import model.Order;
import model.Product;
import model.User;

public interface IUserDao {

	User getUserByID(int id) throws Exception;
	
	void saveUser(User user) throws Exception;
	
	void deleteUser(User user) throws Exception;
	
	void updateUser(User user) throws Exception;
	
	Collection<User> getAllUsers() throws Exception;
	
	Map<Product,LocalDate> getUserProductsById(int userId) throws SQLException;
	
	Set<Integer> getUserFavoritesById(int userId) throws SQLException;
	
	Set<Integer> getUserWatchlistById(int userId) throws SQLException;
	
	User getUserByLoginCredentials(String username, String password) throws SQLException, InvalidUserDataException, InvalidOrderDataException;
	
	void addProductToFavorites(User user, Product product) throws SQLException;
	
	void removeProductFromFavorites(User user, Product product) throws SQLException;
	
	void addProductToWatchlist(User user, Product product) throws SQLException;
	
	void removeProductFromWatchlist(User user, Product product) throws SQLException;
	
	Set <Order> getUserOrdersById(int userId) throws SQLException, InvalidOrderDataException;
	
	void saveUserProductsInCartById(int userId, Map<Product, LocalDate> products) throws SQLException;
}
