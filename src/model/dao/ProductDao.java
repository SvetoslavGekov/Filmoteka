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
import java.util.Map;
import java.util.Set;

import dbManager.DBManager;
import exceptions.InvalidProductDataException;
import model.Genre;
import model.Movie;
import model.Product;
import model.TVSeries;
import util.WebSite;

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
		//Methods is never called for a "product" (synchronization is made for concrete classes)
		try (PreparedStatement ps = con
				.prepareStatement("INSERT INTO products (name, release_year, pg_rating, duration, rent_cost, buy_cost, description,"
						+ "poster, trailer, writers, actors, sale_percent, sale_validity)"
						+ " VALUES (?,YEAR(?),?,?,?,?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);) {
			//Non Mandatory Dates
			LocalDate  saleValidity = p.getSaleValidity();
			
			ps.setString(1, p.getName()); //Name
			ps.setDate(2, Date.valueOf(p.getReleaseDate()));//Release date
			ps.setString(3, p.getPgRating()); // PG Rating
			ps.setInt(4, p.getDuration()); //Duration
			ps.setDouble(5, p.getOriginalRentCost()); //Original Rent cost
			ps.setDouble(6, p.getOriginalBuyCost()); //Original Buy cost
			ps.setString(7, p.getDescription()); // Description
			ps.setString(8, p.getPoster()); //Poster
			ps.setString(9, p.getTrailer()); //Trailer
			ps.setString(10, p.getWriters()); //Writers
			ps.setString(11, p.getActors()); //Actors
			ps.setDouble(12, p.getSalePercent()); //Sale discount percent
			ps.setDate(13, saleValidity != null ? Date.valueOf(saleValidity) : null); //Sale validity
			
			// If the statement is successful --> update product ID
			if (ps.executeUpdate() > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					rs.next();
					p.setId(rs.getInt("GENERATED_KEY"));
				}
			}
			
			//Update the product genres
			saveProductGenresById(p.getId(), p.getGenres());
		}
	}

	@Override
	public void updateProduct(Product p) throws SQLException {
		//Methods is never called for a "product" (synchronization is made for concrete classes)
		// Update the basic information
		try (PreparedStatement ps = con
				.prepareStatement("UPDATE products SET name = ?, release_year = ?, pg_rating = ?,"
						+ " duration = ?, rent_cost = ?, buy_cost = ?, description = ?, poster = ?, trailer = ?, writers = ?,"
						+ "actors = ?, sale_percent = ?, sale_validity = ? WHERE product_id = ?")) {
			//Non Mandatory Dates
			LocalDate saleValidity = p.getSaleValidity();
			
			ps.setString(1, p.getName()); //Name
			ps.setInt(2, p.getReleaseDate().getYear()); //Release year
			ps.setString(3, p.getPgRating()); //PG Rating
			ps.setInt(4, p.getDuration()); //Duration
			ps.setDouble(5, p.getOriginalRentCost()); //Original Rent Cost
			ps.setDouble(6, p.getOriginalBuyCost()); //Original Biu Cost
			ps.setString(7, p.getDescription()); //Description
			ps.setString(8, p.getPoster()); //Poster
			ps.setString(9, p.getTrailer()); //Trailer
			ps.setString(10, p.getWriters());//Writers
			ps.setString(11, p.getActors());//Actors
			ps.setDouble(12, p.getSalePercent());//Sale percent
			ps.setDate(13, (saleValidity != null ? Date.valueOf(saleValidity) : null)); //Validity
			
			ps.setInt(14, p.getId()); //Id
			ps.executeUpdate();
			
			//Update the product genres
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
		try(PreparedStatement ps = con.prepareStatement("SELECT genre_id FROM product_has_genres WHERE product_id = ?;")){
			ps.setInt(1, id);
			try(ResultSet rs = ps.executeQuery()) {
				while(rs.next()) {
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
		
		if(allProducts.isEmpty()) {
			return Collections.emptyList();
		}
		return allProducts;
	}

	public Map<Integer,Double> getProductRatersById(int movieId) throws SQLException {
		Map<Integer,Double> productRaters = new HashMap<>();
		
		try(PreparedStatement ps = con.prepareStatement("SELECT user_id, rating FROM product_has_raters"
				+ " WHERE product_id = ?");){
			ps.setInt(1, movieId);
			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()) {
					productRaters.put(rs.getInt("user_id"), rs.getDouble("rating"));
				}
			}
		}
		return productRaters;
	}
}
