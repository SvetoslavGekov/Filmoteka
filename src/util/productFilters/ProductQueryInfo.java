package util.productFilters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exceptions.InvalidProductQueryInfoException;
import validation.Supp;

public class ProductQueryInfo {
	// Fields
	private String name;
	private int minReleaseYear;
	private int maxReleaseYear;
	private int minDuration;
	private int maxDuration;
	private double minBuyCost;
	private double maxBuyCost;
	private double minRentCost;
	private double maxRentCost;
	private List<Integer> genres = new ArrayList<Integer>();
	private String orderedBy;
	private boolean isAscending;

	public ProductQueryInfo(String name, int minReleaseYear, int maxReleaseYear, int minDuration, int maxDuration,
			double minBuyCost, double maxBuyCost, double minRentCost, double maxRentCost, List<Integer> genres,
			String orderedBy, boolean isAscending) throws InvalidProductQueryInfoException {
		setName(name);
		setMinReleaseYear(minReleaseYear);
		setMaxReleaseYear(maxReleaseYear);
		setMinDuration(minDuration);
		setMaxDuration(maxDuration);
		setMinBuyCost(minBuyCost);
		setMaxBuyCost(maxBuyCost);
		setMinRentCost(minRentCost);
		setMaxRentCost(maxRentCost);
		setGenres(genres);
		setOrderedBy(orderedBy);
		setAscending(isAscending);
	}

	// Setters
	private void setName(String name) {
		this.name = name;
	}

	private void setMinReleaseYear(int minReleaseYear) throws InvalidProductQueryInfoException {
		if (minReleaseYear >= 0) {
			this.minReleaseYear = minReleaseYear;
		}
		else {
			throw new InvalidProductQueryInfoException("Invalid query minimum release year.");
		}
	}

	private void setMaxReleaseYear(int maxReleaseYear) throws InvalidProductQueryInfoException {
		if (maxReleaseYear >= 0) {
			this.maxReleaseYear = maxReleaseYear;
		}
		else {
			throw new InvalidProductQueryInfoException("Invalid query maximum release year.");
		}
	}

	private void setMinDuration(int minDuration) throws InvalidProductQueryInfoException {
		if (minDuration >= 0) {
			this.minDuration = minDuration;
		}
		else {
			throw new InvalidProductQueryInfoException("Invalid query minimum duration.");
		}
	}

	private void setMaxDuration(int maxDuration) throws InvalidProductQueryInfoException {
		if (maxDuration >= 0) {
			this.maxDuration = maxDuration;
		}
		else {
			throw new InvalidProductQueryInfoException("Invalid query maximum duration.");
		}
	}

	private void setMinBuyCost(double minBuyCost) throws InvalidProductQueryInfoException {
		if (minBuyCost >= 0d) {
			this.minBuyCost = minBuyCost;
		}
		else {
			throw new InvalidProductQueryInfoException("Invalid query minimum buy cost.");
		}
	}

	private void setMaxBuyCost(double maxBuyCost) throws InvalidProductQueryInfoException {
		if (maxBuyCost >= 0d) {
			this.maxBuyCost = maxBuyCost;
		}
		else {
			throw new InvalidProductQueryInfoException("Invalid query maximum buy cost.");
		}
	}

	private void setMinRentCost(double minRentCost) throws InvalidProductQueryInfoException {
		if (minRentCost >= 0d) {
			this.minRentCost = minRentCost;
		}
		else {
			throw new InvalidProductQueryInfoException("Invalid query minimum rent cost.");
		}
	}

	private void setMaxRentCost(double maxRentCost) throws InvalidProductQueryInfoException {
		if (maxRentCost >= 0d) {
			this.maxRentCost = maxRentCost;
		}
		else {
			throw new InvalidProductQueryInfoException("Invalid query maximum rent cost.");
		}
	}

	private void setGenres(List<Integer> genres) {
		if (genres != null) {
			this.genres = genres;
		}
	}

	private void setOrderedBy(String orderedBy) throws InvalidProductQueryInfoException {
		if (Supp.isValidStr(orderedBy)) {
			this.orderedBy = orderedBy;
		}
		else {
			throw new InvalidProductQueryInfoException("Invalid query ordering parameter.");
		}
	}
	
	private void setAscending(boolean isAscending) {
		this.isAscending = isAscending;
	}
	
	//Getters


	public String getName() {
		return this.name;
	}

	public int getMinReleaseYear() {
		return this.minReleaseYear;
	}

	public int getMaxReleaseYear() {
		return this.maxReleaseYear;
	}

	public int getMinDuration() {
		return this.minDuration;
	}

	public int getMaxDuration() {
		return this.maxDuration;
	}

	public double getMinBuyCost() {
		return this.minBuyCost;
	}

	public double getMaxBuyCost() {
		return this.maxBuyCost;
	}

	public double getMinRentCost() {
		return this.minRentCost;
	}

	public double getMaxRentCost() {
		return this.maxRentCost;
	}

	public List<Integer> getGenres() {
		return Collections.unmodifiableList(this.genres);
	}

	public String getOrderedBy() {
		return this.orderedBy;
	}

	public boolean isAscending() {
		return this.isAscending;
	}

	@Override
	public String toString() {
		return String.format("Name: %s%n"
				+ "MaxYear: %s%n"
				+ "MinYear: %s%n"
				+ "MaxDuration: %d%n"
				+ "MinDuration: %d%n"
				+ "MaxBuyCost: %.2f%n"
				+ "MinBuyCost: %.2f%n"
				+ "MaxRentCost: %.2f%n"
				+ "MinRentCost: %.2f%n"
				+ "Genres: %s%n"
				+ "OrderedBy: %s%n"
				+ "isAscending: %s%n", name, maxReleaseYear, minReleaseYear, maxDuration, minDuration, maxBuyCost, minBuyCost,
				maxRentCost, minRentCost, genres, orderedBy, isAscending);
	}
}
