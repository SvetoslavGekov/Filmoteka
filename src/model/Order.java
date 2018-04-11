package model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import exceptions.InvalidOrderDataException;

public class Order implements Comparable<Order> {
	// Fields
	private int id;
	private int userId;
	private LocalDate date;
	private double totalCost;
	private Map<Product, LocalDate> shoppingCart = new TreeMap<>();// Key: Product (can be Id) -> Value: Validity date
																	// (null for bought products)

	// Constructors
	// Constructor for saving an order in the DB
	public Order(int userId, LocalDate date, Map<Product, LocalDate> shoppingCart) throws InvalidOrderDataException {
		setUserId(userId);
		setDate(date);
		setShoppingCart(shoppingCart);
		setTotalCost(calculateOrderTotalCost());
	}

	// Constructor for loading an order from the DB
	public Order(int id, int userId, LocalDate date, Map<Product, LocalDate> shoppingCart)
			throws InvalidOrderDataException {
		this(userId, date, shoppingCart);
		setId(id);
	}

	// Setters
	public void setId(int id) throws InvalidOrderDataException {
		if (id >= 0) {
			this.id = id;
		}
		else {
			throw new InvalidOrderDataException("Invalid order id.");
		}
	}

	public void setUserId(int userId) throws InvalidOrderDataException {
		if (userId >= 0) {
			this.userId = userId;
		}
		else {
			throw new InvalidOrderDataException("Invalid order user id.");
		}
	}

	private void setDate(LocalDate date) throws InvalidOrderDataException {
		if (date != null) {
			this.date = date;
		}
		else {
			throw new InvalidOrderDataException("Invalid order date.");
		}
	}

	private void setTotalCost(double totalCost) throws InvalidOrderDataException {
		if (totalCost >= 0) {
			this.totalCost = totalCost;
		}
		else {
			throw new InvalidOrderDataException("Invalid order total cost.");
		}
	}

	private void setShoppingCart(Map<Product, LocalDate> shoppingCart) throws InvalidOrderDataException {
		if (shoppingCart != null) {
			this.shoppingCart = shoppingCart;
		}
		else {
			throw new InvalidOrderDataException("Invalid order products collection.");
		}
	}

	private double calculateOrderTotalCost() {
		double totalCost = 0d;
		for (Entry<Product, LocalDate> e : this.shoppingCart.entrySet()) {
			if (e.getValue() == null) {
				totalCost += e.getKey().getRentCost();
			}
			else {
				totalCost += e.getKey().getBuyCost();
			}
		}
		return totalCost;
	}

	// Getters
	public int getId() {
		return this.id;
	}

	public int getUserId() {
		return this.userId;
	}

	public LocalDate getDate() {
		return this.date;
	}

	public double getTotalCost() {
		return this.totalCost;
	}

	public Map<Product, LocalDate> getShoppingCart() {
		return Collections.unmodifiableMap(this.shoppingCart);
	}

	// Comparators
	@Override
	public int compareTo(Order o) {
		return this.getId() - o.getId();
	}
}
