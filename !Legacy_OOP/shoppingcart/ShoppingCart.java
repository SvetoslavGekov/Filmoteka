package shoppingcart;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import products.Product;

public final class ShoppingCart {
	//Fields
	private Map<Product, Boolean> products = new TreeMap<>(); //Map with products and flag for renting or buying // buy(true) - rent(false)

	//Constructors
	public ShoppingCart() {
		
	}
	
	public ShoppingCart(ShoppingCart cart) {
		this.products = new TreeMap<Product, Boolean>(cart.products);
	}
	
	//Methods
	public void addProduct(Product product, boolean buy){
		if(product != null){
			products.put(product, buy);
		}
	}
	
	public void removeProduct(int idOfProduct){
		if(this.isShoppingCartEmpty()){
			return;
		}
		for (Product product : products.keySet()) {
			if(product.getId() == idOfProduct){
				products.remove(product);
				System.out.println("\nSuccessfully removed " + product.getName() + " from shopping cart");
				return;
			}
		}
		System.out.println("\nNo such a product with this id in your shopping cart!");
	}

	public void showProducts() {
		if(this.isShoppingCartEmpty()){
			return;
		}
		for (Entry<Product, Boolean> entry : products.entrySet()) {
			if(entry.getValue()){
				System.out.println("( BUY )");
			}
			else{
				System.out.println("( RENT )");
			}
			System.out.println(entry.getKey());
		}
	}

	public double getAllProductsPrice(){
		double price = 0;
		for (Entry<Product, Boolean> entry : products.entrySet()) {
			if(entry.getValue()){
				price += entry.getKey().getBuyCost();
			}
			else{
				price += entry.getKey().getRentCost();
			}
		}
		return price;
	}
	
	public boolean buyProductsInCart(double money) {
		if(this.isShoppingCartEmpty()){
			return false;
		}
		if(money >= getAllProductsPrice()){
			System.out.println("\nSuccessfully buy/rented products from cart!");
			return true;
		}
		System.out.println("\nUnsuccessfully buy/rented products from cart! You have not enough money. Try to remove samo of the products.");
		return false;
	}

	public void clearShoppingCart() {
		if(!this.isShoppingCartEmpty()){
			products.clear();
			System.out.println("\nAll products from your shopping cart was removed.");
		}
	}
	
	public boolean isShoppingCartEmpty(){
		if(this.products.isEmpty()){
			System.out.println("\nYour shopping cart is empty!");
			return true;
		}
		return false;
	}
	
	public Map<Product, Boolean> getAllProducts(){
		return Collections.unmodifiableMap(this.products);
	}
}
