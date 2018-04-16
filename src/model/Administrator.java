package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import exceptions.InvalidUserDataException;

public class Administrator extends User {

	public Administrator(String firstName, String lastName, String username, String password, String email)
			throws InvalidUserDataException {
		super(firstName, lastName, username, password, email);
		setAdmin(true);
	}

	public Administrator(int userId, String firstName, String lastName, String username, String password, String email,
			String phone, LocalDate registrationDate, LocalDateTime lastLogin, String profilePicture, double money,
			Set<Integer> favourites, Set<Integer> watchList, Map<Product, LocalDate> products)
			throws InvalidUserDataException {
		super(userId, firstName, lastName, username, password, email, phone, registrationDate, lastLogin,
				profilePicture, money, favourites, watchList, products);
		setAdmin(true);
	}

}
