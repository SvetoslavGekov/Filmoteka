package model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import exceptions.InvalidOrderDataException;

public class Order {
	//Fields
	private int id;
	private User user;
	private LocalDate date;
	private double totalCost;
	private Map<Product, LocalDate> shoppingCart = new TreeMap<>();//Key: Product (can be Id) -> Value: Validity date (null for bought products)

	
	//Constructors
	//Constructor for saving an order in the DB
	public Order(User user, LocalDate date, Map<Product, LocalDate> shoppingCart) throws InvalidOrderDataException {
		setUser(user);
		setDate(date);
		setShoppingCart(shoppingCart);
		setTotalCost(calculateOrderTotalCost());
	}

	//Constructor for loading an order from the DB
	public Order(int id, User user, LocalDate date, Map<Product, LocalDate> shoppingCart) throws InvalidOrderDataException {
		this(user, date, shoppingCart);
		setId(id);
	}

	//Setters
	public void setId(int id) throws InvalidOrderDataException {
		if(id >= 0) {
			this.id = id;
		}
		else {
			throw new InvalidOrderDataException("Invalid order id.");
		}
	}

	private void setUser(User user) throws InvalidOrderDataException {
		if(user != null) {
			this.user = user;
		}
		else {
			throw new InvalidOrderDataException("Invalid order user.");
		}
	}

	private void setDate(LocalDate date) throws InvalidOrderDataException {
		if(date != null) {
			this.date = date;
		}
		else {
			throw new InvalidOrderDataException("Invalid order date.");
		}
	}

	private void setTotalCost(double totalCost) throws InvalidOrderDataException {
		if(totalCost >= 0) {
			this.totalCost = totalCost;
		}
		else {
			throw new InvalidOrderDataException("Invalid order total cost.");
		}
	}

	private void setShoppingCart(Map<Product, LocalDate> shoppingCart) throws InvalidOrderDataException {
		if(shoppingCart != null) {
			this.shoppingCart = shoppingCart;
		}
		else {
			throw new InvalidOrderDataException("Invalid order products collection.");
		}
	}
	
	private double calculateOrderTotalCost() {
		double totalCost = 0d;
		for (Entry<Product,LocalDate> e : this.shoppingCart.entrySet()) {
			if(e.getValue() == null) {
				totalCost += e.getKey().getRentCost();
			}
			else {
				totalCost += e.getKey().getBuyCost();
			}
		}
		return totalCost;
	}

	//Getters
	public int getId() {
		return this.id;
	}

	public User getUser() {
		return this.user;
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
	
}
