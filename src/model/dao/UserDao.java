package model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import dbManager.DBManager;
import exceptions.InvalidOrderDataException;
import exceptions.InvalidUserDataException;
import model.Order;
import model.Product;
import model.User;
import validation.BCrypt;
import webSite.WebSite;

public class UserDao implements IUserDao {

	private static UserDao instance;
	private Connection connection;

	private UserDao() {
		connection = DBManager.getInstance().getCon();
	}

	public static UserDao getInstance() {
		if (instance == null) {
			instance = new UserDao();
		}
		return instance;
	}

	@Override
	public User getUserByID(int id) throws SQLException, InvalidUserDataException, InvalidOrderDataException {
		User user = null;
		String sql = "SELECT user_id, user_type_id, first_name, last_name, username, password, email, phone, registration_date,"
				+ " last_login,profile_picture, money FROM users WHERE user_id = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery();) {
				rs.next();

				int userId = rs.getInt("user_id");
				Timestamp lastLogin = rs.getTimestamp("last_login");
				// Grab users watchlist, favorites and products
				Set<Integer> watchlist = new HashSet<>(getUserWatchlistById(userId));
				Set<Integer> favorites = new HashSet<>(getUserFavoritesById(userId));
				Map<Product, LocalDate> products = new HashMap<>(getUserProductsById(userId));

				user = new User(userId, // User Id
						rs.getInt("user_type_id"), // User type
						rs.getString("first_name"), // First Name
						rs.getString("last_name"), // Last Name
						rs.getString("username"), // UserName
						rs.getString("password"), // Already hashed Password
						rs.getString("email"), // Email
						rs.getString("phone"), // Phone number
						rs.getDate("registration_date").toLocalDate(), // Registration date
						lastLogin != null ? lastLogin.toLocalDateTime() : null, // Last login
						rs.getString("profile_picture"), // Profile picture
						rs.getDouble("money"), // User's money
						favorites, // User's favourite products
						watchlist, // User's watchlist
						products); // User's products

				// Grab user's orders
				Set<Order> orders = new HashSet<>(getUserOrdersById(user.getUserId()));
				// Set user's orders
				user.setOrdersHistory(orders);
			}
		}
		return user;
	}

	@Override
	public void saveUser(User user) throws SQLException {
		PreparedStatement s = connection.prepareStatement(
				"INSERT INTO users (user_type_id, first_name, last_name, username, email, "
						+ "password,registration_date) VALUES (?,?,?,?,?,?,?);",
				PreparedStatement.RETURN_GENERATED_KEYS);
		
		String hashedPassword = user.hashPassword();
		s.setInt(1, user.getUserTypeId()); // User Type
		s.setString(2, user.getFirstName());// First Name
		s.setString(3, user.getLastName()); // Last Name
		s.setString(4, user.getUsername());// Username
		s.setString(5, user.getEmail());// Email
		s.setString(6, hashedPassword);// Pass in hashed password
		s.setDate(7, Date.valueOf(user.getRegistrationDate())); // Registration date

		// If the statement is successful --> update product ID
		if (s.executeUpdate() > 0) {
			try (ResultSet rs = s.getGeneratedKeys()) {
				rs.next();
				user.setUserId(rs.getInt("GENERATED_KEY"));
			}
		}
	}

	@Override
	public void updateUser(User user) throws SQLException {
		String sqlQuery = "UPDATE users SET username = ?, email = ?, password = ?, first_name = ?, last_name = ?, phone = ?, "
				+ "last_login = ?, profile_picture = ?, money = ? WHERE user_id = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
			// Update user info
			LocalDateTime lastLogin = user.getLastLogin();

			ps.setString(1, user.getUsername()); // Username
			ps.setString(2, user.getEmail());// Email
			ps.setString(3, user.getPassword());// Already hashed password
			ps.setString(4, user.getFirstName());// First name
			ps.setString(5, user.getLastName());// Last name
			ps.setString(6, user.getPhone());// Phone
			ps.setTimestamp(7, lastLogin != null ? java.sql.Timestamp.valueOf(lastLogin) : null);// Last login
			ps.setString(8, user.getProfilePicture());// Profile picture
			ps.setDouble(9, user.getMoney());// Money
			ps.setInt(10, user.getUserId());// User id
			ps.executeUpdate();
		}
	}

	@Override
	public void deleteUser(User user) throws SQLException {
		String sqlQuery = "DELETE FROM users WHERE username = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
			ps.setString(1, user.getUsername());
			ps.executeUpdate();
		}
		System.out.println("Successfully deleted account from database.");
	}

	@Override
	public Collection<User> getAllUsers() throws SQLException, InvalidUserDataException, InvalidOrderDataException {
		HashSet<User> resultUsers = new HashSet<>();
		String sql = "SELECT user_id, user_type_id, username, email, password, first_name, last_name, registration_date, phone,"
				+ " last_login, profile_picture, money FROM users ORDER BY user_id DESC;";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					int userId = rs.getInt("user_id");
					Timestamp lastLogin = rs.getTimestamp("last_login");

					// Grab users watchlist, favorites and products
					Set<Integer> watchlist = new HashSet<>(getUserWatchlistById(userId));
					Set<Integer> favorites = new HashSet<>(getUserFavoritesById(userId));
					Map<Product, LocalDate> products = new HashMap<>(getUserProductsById(userId));

					User user = new User(userId, //User id
							rs.getInt("user_type_id"), //User type
							rs.getString("first_name"), //First name
							rs.getString("last_name"),//Last name
							rs.getString("username"),//Username
							rs.getString("password"),//Already hashed password
							rs.getString("email"),//Email
							rs.getString("phone"),//Phone
							rs.getDate("registration_date").toLocalDate(),//Registration date
							lastLogin != null ? lastLogin.toLocalDateTime() : null,//Last login
							rs.getString("profile_picture"),//Profile picture
							rs.getDouble("money"),//Money
							favorites,//Favorites
							watchlist,//Watchlist
							products);//Products

					// Grab user's orders
					Set<Order> orders = new HashSet<>(getUserOrdersById(user.getUserId()));
					// Set user's orders
					user.setOrdersHistory(orders);

					resultUsers.add(user);
				}
			}
		}
		if (resultUsers.isEmpty()) {
			return Collections.emptyList();
		}
		return resultUsers;
	}

	@Override
	public Map<Product, LocalDate> getUserProductsById(int userId) throws SQLException {
		Map<Product, LocalDate> products = new TreeMap<>();
		try (PreparedStatement ps = connection
				.prepareStatement("SELECT product_id, validity FROM user_has_products WHERE user_id = ?;")) {
			ps.setInt(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					// Get products from the website collection
					int productId = rs.getInt("product_id");
					Date validity = rs.getDate("validity");
					Product pr = WebSite.getProductById(productId);
					if (pr == null) {
						break;
					}
					products.put(pr, validity != null ? validity.toLocalDate() : null);
				}
			}
		}
		return products;
	}

	public void saveUserProductsInCartById(int userId, Map<Product, LocalDate> products) throws SQLException {
		try (PreparedStatement ps = connection
				.prepareStatement("INSERT INTO user_has_products (user_id, product_id, validity) VALUES (?,?,?)");) {
			// Set statement parameters for each product and add to batch
			for (Entry<Product, LocalDate> e : products.entrySet()) {
				LocalDate date = e.getValue();

				ps.setInt(1, userId);
				ps.setInt(2, e.getKey().getId());
				ps.setDate(3, date != null ? java.sql.Date.valueOf(date) : null);
				ps.addBatch();
			}
			// Execute entire batch
			ps.executeBatch();
		}
	}

	@Override
	public Set<Integer> getUserFavoritesById(int userId) throws SQLException {
		Set<Integer> favorites = new HashSet<Integer>();
		try (PreparedStatement ps = connection
				.prepareStatement("SELECT product_id FROM user_has_favorite_products WHERE user_id = ?;")) {
			ps.setInt(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					favorites.add(rs.getInt("product_id"));
				}
			}
		}
		return favorites;
	}

	@Override
	public Set<Integer> getUserWatchlistById(int userId) throws SQLException {
		Set<Integer> watchlist = new HashSet<Integer>();
		try (PreparedStatement ps = connection
				.prepareStatement("SELECT product_id FROM user_has_watchlist_products WHERE user_id = ?;")) {
			ps.setInt(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					watchlist.add(rs.getInt("product_id"));
				}
			}
		}
		return watchlist;
	}

	@Override
	public Set<Order> getUserOrdersById(int userId) throws SQLException, InvalidOrderDataException {
		Set<Order> orders = new HashSet<Order>();
		try (PreparedStatement ps = connection.prepareStatement("SELECT 	order_id FROM orders WHERE user_id = ?")) {
			ps.setInt(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Order order = OrderDao.getInstance().getOrderById(rs.getInt("order_id"));
					orders.add(order);
				}
			}
		}
		return orders;
	}

	@Override
	public User getUserByLoginCredentials(String username, String password)
			throws SQLException, InvalidUserDataException, InvalidOrderDataException {
		User user = null;
		try (PreparedStatement ps = connection.prepareStatement("SELECT user_id, password FROM users WHERE username = ?");) {
			ps.setString(1, username);

			try (ResultSet rs = ps.executeQuery();) {
				//If there is such username
				if (rs.next()) {
					//Check the salt of the password
					String hashedPassword = rs.getString("password");
					if(BCrypt.checkpw(password, hashedPassword)) {
						user = getUserByID(rs.getInt(1));
					}
				}
			}
		}
		return user;
	}

	@Override
	public void addProductToFavorites(User user, Product product) throws SQLException {
		try (PreparedStatement ps = connection
				.prepareStatement("INSERT INTO user_has_favorite_products (user_id, product_id) VALUES (?,?);")) {
			ps.setInt(1, user.getUserId());
			ps.setInt(2, product.getId());
			ps.executeUpdate();
		}
	}

	@Override
	public void removeProductFromFavorites(User user, Product product) throws SQLException {
		try (PreparedStatement ps = connection
				.prepareStatement("DELETE FROM user_has_favorite_products WHERE user_id = ? AND product_id = ?;")) {
			ps.setInt(1, user.getUserId());
			ps.setInt(2, product.getId());
			ps.executeUpdate();
		}
	}

	@Override
	public void addProductToWatchlist(User user, Product product) throws SQLException {
		try (PreparedStatement ps = connection
				.prepareStatement("INSERT INTO user_has_watchlist_products (user_id, product_id) VALUES (?,?);")) {
			ps.setInt(1, user.getUserId());
			ps.setInt(2, product.getId());
			ps.executeUpdate();
		}
	}

	@Override
	public void removeProductFromWatchlist(User user, Product product) throws SQLException {
		try (PreparedStatement ps = connection
				.prepareStatement("DELETE FROM user_has_watchlist_products WHERE user_id = ? AND product_id = ?;")) {
			ps.setInt(1, user.getUserId());
			ps.setInt(2, product.getId());
			ps.executeUpdate();
		}
	}

	@Override
	public boolean databaseHasUserWithCredentials(String username, String email) throws SQLException {
		try (PreparedStatement ps = connection
				.prepareStatement("SELECT user_id FROM users WHERE username = ? OR email = ?")) {
			ps.setString(1, username);
			ps.setString(2, email);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return true;
				}
			}
		}
		return false;
	}
}
