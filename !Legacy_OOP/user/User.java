package user;

import java.time.LocalDate;
import java.time.LocalDateTime;

import demo.WebSite;
import products.Product;
import validation.Supp;

public abstract class User implements IUser {
	//Fields
	private static WebSite filmoteka = WebSite.getInstance();
	private String names;
	private String username;
	private String password;
	private String email;
	private String phone = "";
	private LocalDate registrationDate;
	private LocalDateTime lastLogin;

	//Constructors
	public User(String names, String username, String password, String email) {
		this.setNames(names);
		this.setUsername(username);
		this.setPassword(password);
		this.setEmail(email);
		this.registrationDate = LocalDate.now();
	}
	
	//Methods
	@Override
	public String toString() {
		return String.format("User: %s	Names: %s	Email: %s	Phone number: %s", this.username, this.names, this.email, this.phone);
	}
	
	@Override
	public Product searchForProduct() {
		Product product = null;
		System.out.println("\n ========= PRODUCT SEARCH FORM =============");
		System.out.println("Would you like to search for a product by:\n	1) Identification number;\n	2) Name;\n	3) Return to main menu;");
		System.out.print("Please select a search option: ");
		//Get input for option
		int option = Supp.getPositiveNumber();
		switch (option) {
			case 1: product = this.getFilmoteka().findProductById(); break;
			case 2: product = this.getFilmoteka().findProductByName(); break;
			case 3: break;
		default:
			System.out.println("\nYou've chosen an invalid option for this menu. Please try again.");
			return searchForProduct();
		}
		if(product == null && option != 3){
			System.out.println("\n\nNo product matches your search criteria.");
		}
		return product;
	}
	
	@Override
	public void signIn() {
		
		System.out.println("\n========== SIGN IN FORM ===========");
		String username = "";
		String password = "";
		int signInAttempts = 0;
		//Attempt to sign in
		while(signInAttempts < WebSite.MAX_SIGN_IN_ATTEMPTS) {
			//Iterate sign in attempts
			System.out.println("\nSign in attempts remaining: " + (WebSite.MAX_SIGN_IN_ATTEMPTS - signInAttempts));
			signInAttempts++;
			System.out.print("Please enter your username: ");
			username = Supp.inputValidString();
			
			//Check if name is present in the users collection
			if(this.getFilmoteka().checkUserName(username)) {
				System.out.print("Please enter your password: ");
				password = Supp.inputValidString();
				
				//Check if password is correct
				if(this.getFilmoteka().checkUserPassword(username, password)) {
					this.getFilmoteka().loginUser(username);
					return;
				}
			}
			else {
				System.out.printf("There is no user named %s in the %s user database.%n", username, this.getFilmoteka().getName());
			}
		}
		if(signInAttempts >= WebSite.MAX_SIGN_IN_ATTEMPTS) {
			System.out.println("\nYou've expended all your attempts at logging in. Redirecting to main menu.");
//			this.printMainMenu();
		}
	}
	
	@Override
	public void exitApplication() {
		User.filmoteka.stopApp();
	}
	
	@Override
	public void logout() {
		User.filmoteka.logoutUser();
	}
	
	//Setters
	protected boolean setNames(String names){
		if(Supp.validStr(names)){
			this.names = names;
			return true;
		}
		return false;
	}

	protected void setUsername(String username){
		if(Supp.validStr(username)){
			this.username = username;
			return;
		}
	}

	protected boolean setPassword(String password) {
		if(Supp.validStr(password) && Supp.validPassword(password)){
			this.password = password;
			return true;
		}
		return false;
	}

	protected boolean setEmail(String email){
		if(Supp.validStr(email) && Supp.validEmail(email)){
			this.email = email;
			return true;
		}
		return false;
	}

	protected void setPhone(String phone) {
		//Removed the exception --> this is an optional field
		if(Supp.validPhoneNumber(phone)) {
			this.phone = phone;
		}	
	}
	
	//Getters
	public String getUsername() {
		return this.username;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getNames(){
		return this.names;
	}
	
	protected WebSite getFilmoteka() {
		return User.filmoteka;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getPhone() {
		return this.phone;
	}
}
