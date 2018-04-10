package products;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import customexceptions.InvalidProductInfoException;
import validation.Supp;

public final class TVSeries extends Product {
	//Fields
	private LocalDate finishedAiring;
	private int totalSeasons;
	
	//Constructor
	public TVSeries(String name, LocalDate releaseDate, String pgRating, int duration,
			double rentCost, double buyCost) {
		super(name, ProductType.TVSERIES, releaseDate, pgRating, duration, rentCost, buyCost);
	}

	//Methods
	
	public void printFullInfo() {
		super.printFullInfo();
		System.out.printf("Finished airing: %s%n", (this.finishedAiring != null) ? this.finishedAiring : " ");
		System.out.printf("Total seasons: %d%n", this.totalSeasons);
	}
	
	private LocalDate inputFinishedAiringDate() throws InvalidProductInfoException{
		//User inputs product release date
		System.out.print("Please enter the date the series finished airing: ");
		String input = Supp.inputValidString();
		LocalDate releaseDate = null;
		try {
			releaseDate = LocalDate.parse(input);
		}
		catch (DateTimeParseException e) {
			throw new InvalidProductInfoException("You've entered an invalid date.\nDates should have the following format - \"yyyy-mm-dd\"");
		}
		return releaseDate;
	}
	
	private int inputTotalSeasons() {
		//User inputs total seasons 
		System.out.print("Please enter the total number of seasons: ");
		return Supp.getPositiveNumber();
	}
	
	@Override
	public void printProductEditingMenu() {
		//Print product editing menu from superclass
		super.printProductEditingMenu();
		
		//Print advanced menu
		System.out.println("	13) Edit finished airing");
		System.out.println("	14) Edit number of total seasons");
	}
	
	private void editFinishedAiring() {
		System.out.println("\nCurrent finished airing date: " + this.finishedAiring);
		try {
			this.setFinishedAiring(this.inputFinishedAiringDate());
		}
		catch (InvalidProductInfoException e) {
			System.out.println(e.getMessage());
			this.finishedAiring = null;
		}
	}
	
	private void editTotalSeasons() {
		System.out.println("\nCurrent total seasons: " + this.totalSeasons);
		this.setTotalSeasons(this.inputTotalSeasons());
	}
	
	@Override
	protected boolean selectFromAdvancedEditingMenu(int option) {
		switch (option) {
		case 13: this.editFinishedAiring(); return true;
		case 14: this.editTotalSeasons(); return true;
		default:
			System.out.println("You've chosen an invalid option for the TVSeries menu. Please try again.");
			return false;
		}
	}
	
	//Setters
	private void setFinishedAiring(LocalDate finishedAiring) {
		if(finishedAiring != null) {
			this.finishedAiring = finishedAiring;
		}
	}
	
	private void setTotalSeasons(int totalSeasons) {
		if(totalSeasons > 0) {
			this.totalSeasons = totalSeasons;
		}
	}
}
