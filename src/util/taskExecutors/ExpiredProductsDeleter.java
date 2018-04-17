package util.taskExecutors;

import java.sql.SQLException;

import model.dao.ProductDao;

public class ExpiredProductsDeleter implements Runnable {
	//Fields
	private static ExpiredProductsDeleter instance;
	
	//Constructors
	private ExpiredProductsDeleter() {
		
	}
	
	//Methods
	public static ExpiredProductsDeleter getInstance() {
		if(instance == null) {
			instance = new ExpiredProductsDeleter();
		}
		return instance;
	}
	
	@Override
	public void run() {
		try {
			//Deletes all expired products from the user_has_products table in the database
			ProductDao.getInstance().deleteExpiredProducts();
			//Sleep for 5 seconds so we don't start the process a million times over and over again
			Thread.sleep(5000);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
