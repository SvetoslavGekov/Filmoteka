package model.dao.nomenclatures;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import dbManager.DBManager;
import exceptions.InvalidProductCategoryDataException;
import model.nomenclatures.ProductCategory;

public class ProductCategoryDao implements IProductCategoryDao {
	// Fields
	private static ProductCategoryDao instance;
	private Connection con;

	// Constructor
	private ProductCategoryDao() {
		//Create the connection object from the DBManager
		this.con = DBManager.getInstance().getCon();
	}

	// Methods
	public synchronized static ProductCategoryDao getInstance() {
		if (instance == null) {
			instance = new ProductCategoryDao();
		}
		return instance;
	}

	@Override
	public void saveProductCategory(ProductCategory pc) throws SQLException, InvalidProductCategoryDataException {
		try (PreparedStatement ps = con.prepareStatement("INSERT INTO product_categories (value) VALUES(?);",
				PreparedStatement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, pc.getValue());
			// If the insertion is successful
			if (ps.executeUpdate() > 0) {
				// Update the genre's Id
				try (ResultSet rs = ps.getGeneratedKeys()) {
					rs.next();
					pc.setId(rs.getInt("GENERATED_KEY"));
				}
			}
		}
	}

	@Override
	public void updateProductCategory(ProductCategory pc) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement("UPDATE product_categories SET value = ? WHERE category_id = ?;")) {
			ps.setString(1, pc.getValue());
			ps.setInt(2, pc.getId());
			ps.executeUpdate();
		}
	}

	@Override
	public Map<Integer, ProductCategory> getAllProductCategories() throws SQLException, InvalidProductCategoryDataException{
		TreeMap<Integer, ProductCategory> allCategories = new TreeMap<Integer, ProductCategory>();
		try (PreparedStatement ps = con.prepareStatement("SELECT category_id, value FROM product_categories ORDER BY category_id;");) {
			try (ResultSet rs = ps.executeQuery();) {
				// While there are genres to be created
				while (rs.next()) {
					// Create next genre with full data
					ProductCategory pc = new ProductCategory(rs.getInt("category_id"), rs.getString("value"));
					allCategories.put(pc.getId(), pc);
				}
			}
		}
		return allCategories;
	}

}
