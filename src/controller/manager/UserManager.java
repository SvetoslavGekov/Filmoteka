package controller.manager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.InputMismatchException;

import exceptions.InvalidUserDataException;
import model.User;
import model.dao.UserDao;
import webSite.WebSite;

public class UserManager {

	private static UserManager instance;

	private UserManager() {

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
			UserDao.getInstance().saveUser(u);
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
			return UserDao.getInstance().getUserByLoginCredentials(username, password);
		}
		catch (SQLException | InvalidUserDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
