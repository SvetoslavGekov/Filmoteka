package util.taskExecutors;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import exceptions.InvalidUserDataException;
import model.Product;
import model.User;
import model.dao.UserDao;
import util.mailManager.MailManager;

public final class ExpiringProductsNotifier implements Callable<Boolean> {
	//Fields
	private static final String MESSAGE_SUBJECT = "FilmotekaBG - rented products are expiring soon";
	private static ExpiringProductsNotifier instance;
	
	//Constructors
	private ExpiringProductsNotifier() {
		
	}
	
	//Methods
	public static ExpiringProductsNotifier getInstance() {
		if(instance == null) {
			instance = new ExpiringProductsNotifier();
		}
		return instance;
	}
	
	@Override
	public Boolean call() throws SQLException, InvalidUserDataException {
			//Collect all users that need to be notified in a map
			Map<User, List<Product>> expiringProducts = UserDao.getInstance().getExpiringProducts();
			
			//Compose message for each one
			for (Entry<User,List<Product>> e: expiringProducts.entrySet()) {
				User user = e.getKey();
				List<Product> products = e.getValue();
				
				String message = buildEmailMessage(user, products);
				
				//Send each on of them an email (no attachments)
				MailManager.sendEmail(user.getEmail(), MESSAGE_SUBJECT, message, null);
			}
			
		return true;
		
	}
	
	private String buildEmailMessage(User user, List<Product> products) {
		StringBuilder message = new StringBuilder(String.format("Dear %s %s, %n"
				+ "we'd like to remind you that some of the products you've rented are going to expire tomorrow (%s).%n"
				+ "Those products are:%n", user.getFirstName(), user.getLastName(), LocalDate.now().plusDays(1)));
		//Continue composing message for each product
		for (Product product : products) {
			message.append(String.format("	- %s (%s);%n", product.getName(), product.getReleaseDate().getYear()));
		}
		//End the message with a positive vibe
		message.append("With best regards, the FilmotekaBG team.");
		return message.toString();
	}
}
