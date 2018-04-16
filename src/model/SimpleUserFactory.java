package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import exceptions.InvalidUserDataException;

public final class SimpleUserFactory {
	// Constructor
	private SimpleUserFactory() {

	}

	// Methods
	// Create new users for registering in the database
	public static final User createUser(boolean isAdmin, String firstName, String lastName, String username,
			String password, String email) throws InvalidUserDataException {
		User user = null;
		// If isAdmin --> return Admin
		if (isAdmin) {
			user = new Administrator(firstName, lastName, username, password, email);
		}
		// Else --> return user
		else {
			user = new User(firstName, lastName, username, password, email);
		}
		return user;
	}

	// Create new users from reading the database
	public static final User createUser(boolean isAdmin, int userId, String firstName, String lastName, String username, String password,
			String email,String phone, LocalDate registrationDate, LocalDateTime lastLogin, String profilePicture, double money,
			Set<Integer> favourites, Set<Integer> watchList, Map<Product, LocalDate> products) 
			throws InvalidUserDataException {
		User user = null;
		//If isAdmin --> return Admin
		if(isAdmin) {
			user = new Administrator(userId, firstName, lastName, username, password, email, phone, registrationDate, 
					lastLogin, profilePicture, money, favourites, watchList, products);
		}
		//Else --> return user
		else {
			user = new User(userId, firstName, lastName, username, password, email, phone, registrationDate, lastLogin,
					profilePicture, money, favourites, watchList, products);
		}
		return user;
	}
}
