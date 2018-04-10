package validation;

import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public final class Supp {
	//Fields
	private static final int MIN_USERNAME_LENGTH = 4;
	
	//Methods
	public static final int RNG(int max) {
		Random r = new Random();
		return r.nextInt(max);
	}

	public static  final int RNG(int min, int max) {
		Random r = new Random();
		return r.nextInt(max - min) + min;
	}
	
	public static final boolean validStr(String str) {
		return str != null && !str.trim().isEmpty();
	}
	
	public static final boolean validUsername(String username) {
		//Check if username is a validString
		if(!validStr(username)) {
			return false;
		}
		
		//Check for a minimum length of MIN_USERNAME_LENGTH symbols
		if(username.length() < MIN_USERNAME_LENGTH) {
			return false;
		}
		
		//Check if each symbol is a lowercase letter
		for(int i = 0; i < username.length(); i++) {
			char ch = username.charAt(i);
			if(!Character.isLetter(ch)) {
				return false;
			}
			if(Character.isUpperCase(ch)) {
				return false;
			}
		}
		return true;
	}
	
	public static final boolean validEmail(String email){
		//Check if email is a valid string
		if(!validStr(email)) {
			return false;
		}
		
		//Check if email matches the pattern
		Pattern ptr = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		return ptr.matcher(email).matches();
	}
	
	public static final boolean validPassword(String str){
		//Regex source - https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
		Pattern ptr = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$");
		return ptr.matcher(str).matches();
	}
	
	public static final boolean validPhoneNumber(String str){
		return str.length() == 10 && str.charAt(0) == '0' && str.matches("[0-9]+");
	}

	
	public static final String inputValidString() {
		Scanner scan = new Scanner(System.in);
		boolean isValid = false;
		String input = "";
		do {
			input = scan.nextLine().trim();
			isValid = validStr(input);
			if(!isValid) {
				System.out.print("\nPlease enter a valid string: ");
			}
		}while(!isValid);
		return input;
	}
	
	//The result can be null when using this method
	public static final String inputString() {
		Scanner scan = new Scanner(System.in);
		return scan.nextLine();
	}
	
	public static final int getPositiveNumber() {
		Scanner scan = new Scanner(System.in);
		boolean isValid = false;
		int number = -1;
		while(!isValid) {
			while(!scan.hasNextInt()) {
				System.out.println("Please enter a valid positive integer.");
				scan.nextLine();
			}
			number = scan.nextInt();
			if(number > 0) {
				isValid = true;
			}
			else {
				System.out.println("You've entered a negative integer. Try again.");
			}
		}
		return number;
	}
	
	public static final double getDouble() {
		Scanner scan = new Scanner(System.in);
		double number = 0d;
		boolean isValid = false;
		
		while(!isValid) {
			while(!scan.hasNextDouble()) {
				System.out.println("Please enter a valid double value.");
				scan.nextLine();
			}
			number = scan.nextDouble();
			isValid = true;
		}
		
		return number;
	}
	
	//TODO PGRating validation
}

