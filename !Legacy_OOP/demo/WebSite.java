
package demo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import products.Movie;
import products.Product;
import products.TVSeries;
import user.Administrator;
import user.Consumer;
import user.IUser;
import user.User;

public final class WebSite {
	
	//Fields
	public static final int MAX_SIGN_IN_ATTEMPTS = 3;
	private static final String SITE_NAME = "Filmoteka.bg";
	private static final Map<String, IUser> ALL_USERS = new TreeMap<String,IUser>(); //Key: Username, Value: User 
	private static final Collection <Product> CATALOG = new TreeSet<Product>(Product.compareByNameAndReleaseDate);
	private static WebSite instance;
	private static IUser guest;
	private IUser currentUser;
	private int money;
	
	//Constructor
	private WebSite(){
		this.currentUser = WebSite.getGuest();
	}
	
	//Methods
	public void notifyProductDeleters(Product pr) {
		//Notify each user to delete the reference from their
		for (IUser user : ALL_USERS.values()) {
			user.deleteProductFromCollections(pr);
		}
	}
	
	public Product findProductByName() {
		Product product = null;
		String name = Product.inputProductName();
		
		for(Product pr : CATALOG) {
			if(pr.getName().equalsIgnoreCase(name)) {
				product = pr;
				break;
			}
		}
		return product;
	}
	
	public Product findProductById() {
		Product product = null;
		int id = Product.inputProductId();
		
		for (Product pr : CATALOG) {
			if(pr.getId() == id) {
				product = pr;
				break;
			}
		}
		return product;
	}
	
	public boolean checkUserName(String username) {
		if(!WebSite.ALL_USERS.containsKey(username)) {
			return false;
		}
		return true;
	};
	
	public boolean checkUserPassword(String username, String password) {
		if(!WebSite.ALL_USERS.get(username).getPassword().equals(password)) {
			System.out.println("You've entered a wrong password.");
			return false;
		}
		return true;
	}
	
	public void loginUser(String username) {
		//Logs in the user based on his username
		setCurrentUser(WebSite.ALL_USERS.get(username));
		System.out.printf("User: %s has signed in at %s%n", this.currentUser.getUsername(), LocalTime.now());
//		this.currentUser.printMainMenu();
	}
	
	public void logoutUser() {
		//Attempts to logout the current user if not the guest one
		if(this.currentUser != WebSite.guest) {
			System.out.printf("User: %s has logged out at %s%n", this.currentUser.getUsername(), LocalTime.now());
			setCurrentUser(guest);
			System.out.println("Switching to Guest user.");
		}
//		this.currentUser.printMainMenu();
	}
	
	public void stopApp() {
		System.exit(1);
	}
	
	public void registerUser(IUser newUser) {
		//Add user to collection
		WebSite.ALL_USERS.put(newUser.getUsername(), newUser);
		//Sign in the new user
		this.loginUser(newUser.getUsername());
	}
	
	public static IUser getGuest(){
		if(WebSite.guest == null){
			try {
				WebSite.guest = new Consumer("Guest", "Guest", "Guest123", "Guest@filmoteka.bg") ;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return WebSite.guest;
	}
	
	public static WebSite getInstance(){
		if(instance == null){
			instance = new WebSite();
		}
		return instance;
	}
	
	//Setters
	public void setCurrentUser(IUser currentUser) {
		if(currentUser != null) {
			this.currentUser = currentUser;
		}
	}
	
	//Getters
	
	public String getName() {
		return WebSite.SITE_NAME;
	}
	
	public IUser getCurrentUser() {
		return this.currentUser;
	}
	
	public static Collection<Product> getCatalog() {
		return Collections.unmodifiableCollection(WebSite.CATALOG);
	}
	
	public void addProductToCatalog(Product product) {
		if(product != null){
			CATALOG.add(product);
		}
	}
	
	public void removeProductFromCatalog(Product product) {
		if(product != null){
			CATALOG.remove(product);
		}
	}
	
	//Demonstration starts from here
	public static void main(String[] args) {
		while(true) {
			WebSite.getInstance().getCurrentUser().printMainMenu();
			WebSite.getInstance().getCurrentUser().selectFromMainMenu();
		}
	}
	
	
	//Static block for filling collections with initial data
	
	static {
		HashSet<User> initalUsers = new HashSet<>();
		TreeSet<Product> initialProducts = new TreeSet<>(Product.compareByNameAndReleaseDate);
		try {
			//Creation of 3 users
			initalUsers.add(new Consumer("Svetoslav Gekov", "sgekov", "Sgekov123", "svetoslav_gekov@abv.bg"));
			initalUsers.add(new Consumer("Mario Dimitrov", "mdimitrov", "Mdimitrov123", "mario0.bg@abv.bg"));
			initalUsers.add(new Administrator("Admin Adminov", "admin", "Admin123", "admin@filmoteka.bg"));
			
			for (User user : initalUsers) {
				WebSite.ALL_USERS.put(user.getUsername(), user);
			}
			
			initialProducts.add(new Movie("Titanic", LocalDate.parse("1999-10-10"), "PG-13", 123, 5d, 10d));
			initialProducts.add(new Movie("Terminator", LocalDate.parse("2000-05-05"), "PG-16", 99, 10d, 15d));
			initialProducts.add(new Movie("Jumanji", LocalDate.parse("1998-11-10"), "PG-12", 89, 5d, 10d));
			
			initialProducts.add(new TVSeries("The Simpsons", LocalDate.parse("2001-02-10"), "PG-13", 22, 2d, 7d));
			initialProducts.add(new TVSeries("ER", LocalDate.parse("1995-01-10"), "PG-12", 45, 5d, 10d));
			initialProducts.add(new TVSeries("The Mentalist", LocalDate.parse("2007-03-14"), "PG-12", 42, 6d, 10d));
			
			WebSite.CATALOG.addAll(initialProducts);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
