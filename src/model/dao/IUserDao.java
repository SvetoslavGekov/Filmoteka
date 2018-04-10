package model.dao;

import java.util.Collection;

import model.User;

public interface IUserDao {

	User getUserByID(int id) throws Exception;
	
	void deleteUser(User user) throws Exception;
	
	void saveUser(User user) throws Exception;
	
	void updateUser(User user) throws Exception;
	
	Collection<User> getAllUsers() throws Exception;
	
}
