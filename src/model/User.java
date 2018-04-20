package model;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import exceptions.InvalidUserDataException;
import validation.BCrypt;
import validation.Supp;

public class User implements Comparable<User> {
	// Fields
	private static final int RENT_PERIOD = 8; // days
	private int userId;
	private boolean isAdmin;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String email;
	private String phone = "";
	private LocalDate registrationDate;
	private LocalDateTime lastLogin;
	private String profilePicture;
	private double money;

	// Collections
	private Set<Integer> favourites = new TreeSet<>(); // Set of productId's
	private Set<Integer> watchList = new TreeSet<>(); // Set of productId's
	private Map<Product, LocalDate> products = new TreeMap<>(); // Key: Product (can be Id) -> Value: Validity date
																// (null for bought products)
	private TreeMap<Product, LocalDate> shoppingCart = new TreeMap<>(); // Same as products
	private Set <Order> ordersHistory = new TreeSet<>();

	// Constructors
	// Constructor for registering a new user
	public User(String firstName, String lastName, String username, String password, String email)
			throws InvalidUserDataException {
		setAdmin(false);
		setFirstName(firstName);
		setLastName(lastName);
		setUsername(username);
		setPassword(password);
		setEmail(email);
		setRegistrationDate(LocalDate.now());
	}

	// Constructor for reading a user from the DB
	public User(int userId, String firstName, String lastName, String username, String password,
			String email, String phone, LocalDate registrationDate, LocalDateTime lastLogin, String profilePicture,
			double money, Set<Integer> favourites, Set<Integer> watchList, Map<Product, LocalDate> products)
			throws InvalidUserDataException {
		this(firstName, lastName, username, password, email);
		setUserId(userId);
		setAdmin(false);
		setPhone(phone);
		setRegistrationDate(registrationDate);
		setLastLogin(lastLogin);
		setProfilePicture(profilePicture);
		setMoney(money);
		setFavourites(favourites);
		setWatchList(watchList);
		setProducts(products);
	}

	// Getters and Setters
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		if (userId > 0) {
			this.userId = userId;
		}
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isAdmin() {
		return this.isAdmin;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) throws InvalidUserDataException {
		if (Supp.isValidStr(firstName)) {
			this.firstName = firstName;
		}
		else {
			throw new InvalidUserDataException("Invalid user first name.");
		}
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) throws InvalidUserDataException {
		if (Supp.isValidStr(lastName)) {
			this.lastName = lastName;
		}
		else {
			throw new InvalidUserDataException("Invalid user last name.");
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) throws InvalidUserDataException {
		if (Supp.isValidStr(username) && Supp.isValidUsername(username)) {
			this.username = username;
		}
		else {
			throw new InvalidUserDataException("Invalid username.");
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws InvalidUserDataException {
		if (Supp.isValidStr(password)) {
			this.password = password;
		}
		else {
			throw new InvalidUserDataException("Invalid user password.");
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) throws InvalidUserDataException {
		if (Supp.isValidStr(email) && Supp.isValidEmail(email)) {
			this.email = email;
		}
		else {
			throw new InvalidUserDataException("Invalid user email.");
		}
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) throws InvalidUserDataException {
		if(phone == null || Supp.isValidPhoneNumber(phone)){
			this.phone = phone;
		}
		else {
			throw new InvalidUserDataException("Invalid user's phone number!");
		}
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) throws InputMismatchException {
		if (money >= 0) {
			this.money = money;
		}
	}

	public LocalDate getRegistrationDate() {
		return this.registrationDate;
	}

	private void setRegistrationDate(LocalDate registrationDate) {
		if (registrationDate != null) {
			this.registrationDate = registrationDate;
		}
	}

	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(LocalDateTime lastLogin) {
		if (lastLogin != null) {
			this.lastLogin = lastLogin;
		}
	}

	public String getProfilePicture() {
		return this.profilePicture;
	}

	public void setProfilePicture(String profilePicture) throws InvalidUserDataException {
		try {
			if (Supp.isValidImagePath(profilePicture)) {
				this.profilePicture = profilePicture;
			}
		}
		catch (IOException e) {
			throw new InvalidUserDataException("Invalid image path");
		}
	}

	public void setFavourites(Set<Integer> favourites) {
		if (favourites != null) {
			this.favourites = favourites;
		}
	}

	public Set<Integer> getFavourites() {
		return Collections.unmodifiableSet(this.favourites);
	}

	public void setWatchList(Set<Integer> watchList) {
		if (watchList != null) {
			this.watchList = watchList;
		}
	}

	public Set<Integer> getWatchList() {
		return Collections.unmodifiableSet(this.watchList);
	}

	public void setProducts(Map<Product, LocalDate> products) {
		if (products != null) {
			this.products = products;
		}
	}

	public Map<Product, LocalDate> getProducts() {
		return Collections.unmodifiableMap(this.products);
	}

	public void removeFavoriteProduct(Integer productId) {
		this.favourites.remove(productId);
	}

	public void addFavoriteProduct(Integer productId) {
		this.favourites.add(productId);
	}

	public void removeWatchlistProduct(Integer productId) {
		this.watchList.remove(productId);
	}

	public void addWatchlistProduct(Integer productId) {
		this.watchList.add(productId);
	}
	
	public void addProductsFromShoppingCart() {
		this.products.putAll(this.shoppingCart);
	}
	
	public void addProductToCart(Product product, boolean willBuy) {
		if (product != null) {
			// Add product as rented if willBuy is false (product validity is current date +
			// RENT_PERIOD)
			this.shoppingCart.put(product, willBuy ? null : LocalDate.now().plusDays(User.RENT_PERIOD));
		}
	}

	public void removeProductFromCart(Product product) {
		if (product != null) {
			this.shoppingCart.remove(product);
		}
	}

	public void cleanCart() {
		this.shoppingCart.clear();
	}

	public Map<Product, LocalDate> getShoppingCart() {
		return Collections.unmodifiableMap(this.shoppingCart);
	}

	public double getShoppingCartPrice() {
		double totalCost = 0d;
		for (Entry<Product, LocalDate> e : this.shoppingCart.entrySet()) {
			if (e.getValue() == null) {
				totalCost += e.getKey().getRentCost();
			}
			else {
				totalCost += e.getKey().getBuyCost();
			}
		}
		return totalCost;
	}

	public void setOrdersHistory(Set<Order> ordersHistory) {
		if(ordersHistory != null) {
			this.ordersHistory = ordersHistory;
		}
	}
	
	public Set<Order> getOrdersHistory() {
		return Collections.unmodifiableSet(this.ordersHistory);
	}
	
	public void addOrder(Order order) {
		if(order != null) {
			this.ordersHistory.add(order);
		}
	}
	
	public boolean ownsProduct(Product product) {
		//Check if user has bought this product
		if(this.products.containsKey(product)) {
			return true;
		}
		return false;
	}
	
	public String hashPassword() {
		return BCrypt.hashpw(this.password, BCrypt.gensalt());
	}

	@Override
	public int compareTo(User o) {
		return this.username.compareTo(o.getUsername());
	}
	
	@Override
	public String toString() {
		return String.format(
				"ID: %d\tUser: %s\tFirstName: %s\tLastName: %s \t Email: %s\tPhone number: %s\tRegDate: %s\tLastLogin: %s"
						+ "%n%nProducts: %s %n%nFavorites: %s %n%nWatchlist: %s",
				this.userId, this.username, this.firstName, this.lastName, this.email, this.phone,
				this.registrationDate, this.lastLogin, this.products, this.favourites, this.watchList);
	}



}
