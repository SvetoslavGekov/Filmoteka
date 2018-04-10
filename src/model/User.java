package model;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import exceptions.InvalidUserDataException;
import validation.Supp;

public class User {
	// Fields
	private int userId;
	private int userTypeId;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String email;
	private String phone = "";
	private LocalDate registrationDate;
	private LocalDateTime lastLogin;// TODO LoacalDateTime
	private String profilePicture;
	private double money;

	// Collections
	private Set<Integer> favourites = new TreeSet<>();
	private Set<Integer> watchList = new TreeSet<>();
	private Map<Product, LocalDate> products = new HashMap<>();
	// private Collection <Order> ordersHistory = new TreeSet<>();;
	// private ShoppingCart shoppingCart = new ShoppingCart();

	// Constructors
	// Constructor for registering a new user
	public User(String firstName, String lastName, String username, String password, String email)
			throws InvalidUserDataException {
		setUserTypeId(2);
		setFirstName(firstName);
		setLastName(lastName);
		setUsername(username);
		setPassword(password);
		setEmail(email);
		setRegistrationDate(LocalDate.now());
	}

	// Constructor for reading a user from the DB
	public User(int userId, int userTypeId, String firstName, String lastName, String username, String password,
			String email, String phone, LocalDate registrationDate, LocalDateTime lastLogin, String profilePicture,
			double money, Set<Integer> favourites, Set<Integer> watchList,
			Map<Product, LocalDate> products) throws InvalidUserDataException {
		this(firstName, lastName, username, password, email);
		setUserId(userId);
		setUserTypeId(userTypeId);
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

	public int getUserTypeId() {
		return userTypeId;
	}

	protected void setUserTypeId(int userTypeId) throws InputMismatchException {
		if (userTypeId == 1 || userTypeId == 2) {
			this.userTypeId = userTypeId;
		}

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
		if (Supp.isValidStr(password) && Supp.isValidPassword(password)) {
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

	public void setPhone(String phone) {
		this.phone = phone;
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
		this.registrationDate = registrationDate;
	}

	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
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
		this.favourites = favourites;
	}

	public Set<Integer> getFavourites() {
		return this.favourites;
	}

	public void setWatchList(Set<Integer> watchList) {
		this.watchList = watchList;
	}

	public Set<Integer> getWatchList() {
		return this.watchList;
	}

	public void setProducts(Map<Product, LocalDate> products) {
		this.products = products;
	}

	public Map<Product, LocalDate> getProducts() {
		return this.products;
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
