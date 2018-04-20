package model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dbManager.DBManager;
import exceptions.InvalidProductDataException;
import model.Movie;
import model.Product;
import model.TVSeries;
import model.User;
import model.nomenclatures.Genre;
import util.WebSite;
import util.productFilters.ProductQueryInfo;

public final class ProductDao implements IProductDao {
	// Fields
	private static ProductDao instance;
	private Connection con;

	// Constructors
	private ProductDao() {
		// Create the connection object from the DBManager
		this.con = DBManager.getInstance().getCon();
	}

	// Methods
	public synchronized static ProductDao getInstance() {
		if (instance == null) {
			instance = new ProductDao();
		}
		return instance;
	}

	@Override
	public void saveProduct(Product p) throws SQLException, InvalidProductDataException {
		// Methods is never called for a "product" (synchronization is made for concrete
		// classes)
		try (PreparedStatement ps = con.prepareStatement(
				"INSERT INTO products (name, release_year, pg_rating, duration, rent_cost, buy_cost, description,"
						+ "poster, trailer, writers, actors, sale_percent, sale_validity)"
						+ " VALUES (?,YEAR(?),?,?,?,?,?,?,?,?,?,?,?)",
				PreparedStatement.RETURN_GENERATED_KEYS);) {
			// Non Mandatory Dates
			LocalDate saleValidity = p.getSaleValidity();

			ps.setString(1, p.getName()); // Name
			ps.setDate(2, Date.valueOf(p.getReleaseDate()));// Release date
			ps.setString(3, p.getPgRating()); // PG Rating
			ps.setInt(4, p.getDuration()); // Duration
			ps.setDouble(5, p.getOriginalRentCost()); // Original Rent cost
			ps.setDouble(6, p.getOriginalBuyCost()); // Original Buy cost
			ps.setString(7, p.getDescription()); // Description
			ps.setString(8, p.getPoster()); // Poster
			ps.setString(9, p.getTrailer()); // Trailer
			ps.setString(10, p.getWriters()); // Writers
			ps.setString(11, p.getActors()); // Actors
			ps.setDouble(12, p.getSalePercent()); // Sale discount percent
			ps.setDate(13, saleValidity != null ? Date.valueOf(saleValidity) : null); // Sale validity

			// If the statement is successful --> update product ID
			if (ps.executeUpdate() > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					rs.next();
					p.setId(rs.getInt("GENERATED_KEY"));
				}
			}

			// Update the product genres
			saveProductGenresById(p.getId(), p.getGenres());
		}
	}

	@Override
	public void updateProduct(Product p) throws SQLException {
		// Methods is never called for a "product" (synchronization is made for concrete
		// classes)
		// Update the basic information
		try (PreparedStatement ps = con
				.prepareStatement("UPDATE products SET name = ?, release_year = ?, pg_rating = ?,"
						+ " duration = ?, rent_cost = ?, buy_cost = ?, description = ?, poster = ?, trailer = ?, writers = ?,"
						+ "actors = ?, sale_percent = ?, sale_validity = ? WHERE product_id = ?")) {
			// Non Mandatory Dates
			LocalDate saleValidity = p.getSaleValidity();

			ps.setString(1, p.getName()); // Name
			ps.setInt(2, p.getReleaseDate().getYear()); // Release year
			ps.setString(3, p.getPgRating()); // PG Rating
			ps.setInt(4, p.getDuration()); // Duration
			ps.setDouble(5, p.getOriginalRentCost()); // Original Rent Cost
			ps.setDouble(6, p.getOriginalBuyCost()); // Original Biu Cost
			ps.setString(7, p.getDescription()); // Description
			ps.setString(8, p.getPoster()); // Poster
			ps.setString(9, p.getTrailer()); // Trailer
			ps.setString(10, p.getWriters());// Writers
			ps.setString(11, p.getActors());// Actors
			ps.setDouble(12, p.getSalePercent());// Sale percent
			ps.setDate(13, (saleValidity != null ? Date.valueOf(saleValidity) : null)); // Validity

			ps.setInt(14, p.getId()); // Id
			ps.executeUpdate();

			// Update the product genres
			saveProductGenresById(p.getId(), p.getGenres());
		}

	}

	private void saveProductGenresById(int productId, Set<Genre> genres) throws SQLException {
		// Delete all the product's genres
		try (PreparedStatement ps = con.prepareStatement("DELETE FROM product_has_genres WHERE product_id = ?;")) {
			ps.setInt(1, productId);
			ps.executeUpdate();
		}

		// Update all the genres if any
		Set<Genre> pGenres = genres;
		if (!pGenres.isEmpty()) {
			Statement st = con.createStatement();
			for (Genre genre : pGenres) {
				st.addBatch(String.format("INSERT INTO product_has_genres VALUES(%d,%d);", productId, genre.getId()));
			}
			st.executeBatch();
		}
	}

	@Override
	public Collection<Genre> getProductGenresById(int id) throws SQLException {
		Collection<Genre> productGenres = new HashSet<>();
		try (PreparedStatement ps = con
				.prepareStatement("SELECT genre_id FROM product_has_genres WHERE product_id = ?;")) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Genre g = WebSite.getGenreById(rs.getInt("genre_id"));
					productGenres.add(g);
				}
			}
		}
		return productGenres;
	}

	@Override
	public Collection<Product> getAllProducts() throws SQLException, InvalidProductDataException {

		Collection<Movie> movies = MovieDao.getInstance().getAllMovies();
		Collection<TVSeries> tvseries = TVSeriesDao.getInstance().getAllTVSeries();

		Collection<Product> allProducts = new ArrayList<>();
		allProducts.addAll(movies);
		allProducts.addAll(tvseries);

		if (allProducts.isEmpty()) {
			return Collections.emptyList();
		}
		return allProducts;
	}

	@Override
	public Map<Integer, Double> getProductRatersById(int movieId) throws SQLException {
		Map<Integer, Double> productRaters = new HashMap<>();

		try (PreparedStatement ps = con
				.prepareStatement("SELECT user_id, rating FROM product_has_raters" + " WHERE product_id = ?");) {
			ps.setInt(1, movieId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					productRaters.put(rs.getInt("user_id"), rs.getDouble("rating"));
				}
			}
		}
		return productRaters;
	}

	@Override
	public void deleteExpiredProducts() throws SQLException {
		String sql = "DELETE FROM user_has_products WHERE validity < CURDATE();";
		try (Statement st = con.createStatement()) {
			st.executeUpdate(sql);
		}
	}

	@Override
	public List<Integer> getFilteredProducts(ProductQueryInfo filter) throws SQLException{
		List<Integer> filteredProducts = new ArrayList<>();
		
		//Building the query
		StringBuilder query = new StringBuilder("SELECT p.product_id, p.release_year, p.duration, p.rent_cost,"
				+ " p.buy_cost, p.name FROM products AS p" 
				+ "	JOIN product_has_genres USING(product_id)"
				+ " JOIN genres AS g USING(genre_id) " 
				+ "	WHERE (p.name IS NULL OR p.name LIKE ?)" 
				+ "	AND (p.release_year IS NULL OR (p.release_year >= ? AND p.release_year <= ?))" 
				+ "	AND (p.duration IS NULL OR (p.duration >= ? AND p.duration <= ?))" 
				+ "	AND (p.buy_cost IS NULL OR (p.buy_cost >= ? AND p.buy_cost <= ?))" 
				+ "	AND (p.rent_cost IS NULL OR (p.rent_cost >= ? AND p.rent_cost <= ?)) ");
				
		//Add genres if any
		List<Integer> genres = filter.getGenres();
		if(!genres.isEmpty()) {
			query.append("AND (g.genre_id IS NULL OR g.genre_id IN(");
			for (int i = 0; i < genres.size(); i++) {
				if(i != genres.size() - 1) {
					query.append("?,");
				}
				else {
					query.append("?)) ");
				}
			}
		}
		
		//Add the ordering part (
		query.append("GROUP BY p.name ORDER BY ? " + (filter.isAscending() ? "ASC" : "DESC"));
		
		//Initialize a counter for setting the parameters
		int paramCounter = 1;
		
		try(PreparedStatement ps = con.prepareStatement(query.toString())){
			String name = filter.getName();
			//Set the first set of parameters
			ps.setString(paramCounter++, name != null ? "%"+name+"%" : "%%"); //Like name
			ps.setInt(paramCounter++, filter.getMinReleaseYear()); //Min release year
			ps.setInt(paramCounter++, filter.getMaxReleaseYear()); //Max release year
			ps.setInt(paramCounter++, filter.getMinDuration()); //Min duration
			ps.setInt(paramCounter++, filter.getMaxDuration()); //Max duration
			ps.setDouble(paramCounter++, filter.getMinBuyCost());// Min buy cost
			ps.setDouble(paramCounter++, filter.getMaxBuyCost());// Max buy cost
			ps.setDouble(paramCounter++, filter.getMinRentCost());// Min rent cost
			ps.setDouble(paramCounter++, filter.getMaxRentCost());// Max rent cost
			
			//Setting the IN clause for the genres if any
			for (Integer genre_id : genres) {
				ps.setInt(paramCounter++, genre_id);
			}
			
			//Setting the final set of parameters
			ps.setString(paramCounter++, filter.getOrderedBy());
			
			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()) {
					filteredProducts.add(rs.getInt("product_id"));
				}
			}
		}
		return filteredProducts;
	}

	public void rateProduct(User user, Product product, int rating) throws SQLException{
		
		// Useless checking but recommended
		if(product != null && user != null){
			// Add the rater so can get the calculated rating
			product.addRater(user, rating);
		}
		
		double newRating = product.calculateRating();
		
		String updateRatingOfProduct = "UPDATE product_has_raters SET rating = ? WHERE product_id = ? AND user_id = ?;";
		String insertRatingOfProduct = "INSERT INTO product_has_raters(product_id, user_id, rating) VALUES(?,?,?);";
		// Check if this user already has rated the product
		try(PreparedStatement ps1 = con.prepareStatement(updateRatingOfProduct)){
			ps1.setDouble(1, newRating);
			ps1.setInt(2, product.getId());
			ps1.setInt(3, user.getUserId());
			// Get affected rows
			int rowsAffected = ps1.executeUpdate();
			
			// If User has not been rate this product (affected rows are 0)
			if(rowsAffected < 1){
				// Insert the new rater into the database
				try(PreparedStatement ps2 = con.prepareStatement(insertRatingOfProduct);){
					ps2.setInt(1, product.getId());
					ps2.setInt(2, user.getUserId());
					ps2.setDouble(3, newRating);
				}	
			}	
		}
	}
}
