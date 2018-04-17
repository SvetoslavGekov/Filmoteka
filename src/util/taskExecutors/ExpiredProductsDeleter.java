package util.taskExecutors;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import model.dao.ProductDao;

public class ExpiredProductsDeleter implements Callable<Boolean> {
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
	public Boolean call() throws SQLException, InterruptedException {
		//Deletes all expired products from the user_has_products table in the database
		ProductDao.getInstance().deleteExpiredProducts();
		//Sleep for 0.5 seconds so we don't start the process a million times over and over again
		Thread.sleep(500);
		return true;
	}
	
	
}
