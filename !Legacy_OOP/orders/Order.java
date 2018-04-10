package orders;

import java.time.LocalDate;
import shoppingcart.ShoppingCart;
import user.IConsumer;

public final class Order implements Comparable<Order> {
	//Fields
	private static int currentID;
	
	private int id;
	private LocalDate date;
	private IConsumer consumer;
	private ShoppingCart cart;
	private double totalCost;
	
	//Constructor
	public Order(IConsumer consumer, ShoppingCart cart) {
		setId();
		setDate(LocalDate.now());
		setConsumer(consumer);
		setCart(cart);
		setTotalCost(this.cart.getAllProductsPrice());
	}
	
	//Methods
	@Override
	public int compareTo(Order o) {
		return this.id - o.id;
	}
	
	public void printInfo() {
		System.out.printf("%nOrder ID: %d%n", this.id);
		System.out.printf("Date of purchase: %s%n", this.date);
		System.out.printf("Buyer: %s%n", this.consumer);
		System.out.printf("Total cost: %.2f%n", this.totalCost);
		this.cart.showProducts();
	}
	
	//Setters
	private void setTotalCost(double totalCost) {
		if(totalCost > 0d) {
			this.totalCost = totalCost;
		}
	}
	
	private void setCart(ShoppingCart cart) {
		if(cart != null) {
			this.cart = cart;
		}
	}
	
	private void setConsumer(IConsumer consumer) {
		if(consumer != null) {
			this.consumer = consumer;
		}
	}
	
	private void setDate(LocalDate date) {
		if(date != null) {
			this.date = date;
		}
	}
	
	private void setId() {
		this.id = currentID++;
	}
}
