package model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import dbManager.DBManager;
import exceptions.InvalidOrderDataException;
import model.Order;
import model.Product;
import util.WebSite;

public final class OrderDao implements IOrderDao {
	//Fields
	private static OrderDao instance;
	private Connection con;
	
	//Constructors
	private OrderDao() {
		// Create the connection object from the DBManager
		this.con = DBManager.getInstance().getCon();  
	}
	
	//Methods
	public synchronized static OrderDao getInstance() {
		if(instance == null) {
			instance = new OrderDao();
		}
		return instance;
	}
	
	public Order getOrderById(int orderId) throws SQLException, InvalidOrderDataException {
		Order order = null;
		
		//Get the collection of products
		Map<Product, LocalDate> products = getOrderProductsById(orderId);
		
		//Get the basic order information
		try(PreparedStatement ps = con.prepareStatement("SELECT order_id, user_id, date, total_cost FROM orders WHERE order_id = ?;")){
			ps.setInt(1, orderId);
			try(ResultSet rs = ps.executeQuery()){
				//Grab the user by his Id
				rs.next();
				Date date = rs.getDate("date");
				int userId = rs.getInt("user_id");
				//Create order object
				order = new Order(orderId, userId, date != null ? date.toLocalDate() : null, products);
			}
		}
		return order;
	}
	
	private Map<Product, LocalDate> getOrderProductsById(int orderId) throws SQLException {
		Map<Product, LocalDate> products = new TreeMap<>();
		
		try(PreparedStatement ps = con.prepareStatement("SELECT order_id, product_id, validity FROM order_has_products WHERE order_id = ?;")){
			ps.setInt(1, orderId);
			try(ResultSet rs = ps.executeQuery()){
				//Populate products collection
				while(rs.next()) {
					Product pr = WebSite.getProductById(rs.getInt("product_id"));
					Date validity = rs.getDate("validity");
					
					products.put(pr, validity != null ? validity.toLocalDate() : null);
				}
			}
		}
		
		return products;
	}

	public void saveOrder(Order order) throws SQLException, InvalidOrderDataException {
		synchronized (con) {
			//Set autocommiting to false
			con.setAutoCommit(false);
			
			//Add order to the orders table
			try(PreparedStatement ps = con.prepareStatement("INSERT INTO orders (user_id, date, total_cost) VALUES (?, ?, ?);",
					PreparedStatement.RETURN_GENERATED_KEYS)) {
				ps.setInt(1, order.getUserId());
				ps.setDate(2, java.sql.Date.valueOf(order.getDate()));
				ps.setDouble(3, order.getTotalCost());
				
				if(ps.executeUpdate() > 0) {
					//Get generated Id
					try(ResultSet rs = ps.getGeneratedKeys()){
						rs.next();
						order.setId(rs.getInt("GENERATED_KEY"));
					}
				}
				
				//Add the products to the order_has_products table
				saveOrderProducts(order);
				
				//If all the queries are successful
				con.commit();
			}
			catch (SQLException e) {
				//Rollback DB and rethrow the exception
				con.rollback();
				throw e;
			}
			finally {
				//Return the autocommiting to true
				con.setAutoCommit(true);
			}
		}
	}
	
	private void saveOrderProducts(Order order) throws SQLException {
		//Get orderId and collection of products
		int orderId = order.getId();
		Map<Product,LocalDate> products = order.getShoppingCart();
		
		try(PreparedStatement ps = con.prepareStatement("INSERT INTO order_has_products (order_id, product_id, validity) VALUES (?, ?, ?);")){
			//Set statement parameters for each product and add to batch
			for (Entry<Product, LocalDate> e : products.entrySet()) {
				LocalDate date = e.getValue();
				
				ps.setInt(1, orderId);
				ps.setInt(2, e.getKey().getId());
				ps.setDate(3, date != null ? java.sql.Date.valueOf(date) : null);
				ps.addBatch();
			}
			//Execute entire batch
			ps.executeBatch();
		}
	}
}
