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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import dbManager.DBManager;
import exceptions.InvalidProductDataException;
import exceptions.InvalidProductQueryInfoException;
import model.Product;
import model.SimpleProductFactory;
import model.User;
import model.nomenclatures.Genre;
import model.nomenclatures.ProductCategory;
import util.WebSite;
import util.productFilters.ProductQueryInfo;
import validation.Supp;

public final class ProductDao implements IProductDao {
	private static final String DEFAULT_FILTER_ORDERED_BY = "name";
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
				"INSERT INTO products (name, category_id, release_year, pg_rating, duration, rent_cost, buy_cost, description,"
						+ "poster, trailer, writers, actors, sale_percent, sale_validity)"
						+ " VALUES (?,?,YEAR(?),?,?,?,?,?,?,?,?,?,?,?)",
				PreparedStatement.RETURN_GENERATED_KEYS);) {
			// Non Mandatory Dates
			LocalDate saleValidity = p.getSaleValidity();
			int paramCounter = 1;
			
			ps.setString(paramCounter++, p.getName()); // Name
			ps.setInt(paramCounter++, p.getProductCategory().getId()); // Product category
			ps.setDate(paramCounter++, Date.valueOf(p.getReleaseDate()));// Release date
			ps.setString(paramCounter++, p.getPgRating()); // PG Rating
			ps.setInt(paramCounter++, p.getDuration()); // Duration
			ps.setDouble(paramCounter++, p.getOriginalRentCost()); // Original Rent cost
			ps.setDouble(paramCounter++, p.getOriginalBuyCost()); // Original Buy cost
			ps.setString(paramCounter++, p.getDescription()); // Description
			ps.setString(paramCounter++, p.getPoster()); // Poster
			ps.setString(paramCounter++, p.getTrailer()); // Trailer
			ps.setString(paramCounter++, p.getWriters()); // Writers
			ps.setString(paramCounter++, p.getActors()); // Actors
			ps.setDouble(paramCounter++, p.getSalePercent()); // Sale discount percent
			ps.setDate(paramCounter++, saleValidity != null ? Date.valueOf(saleValidity) : null); // Sale validity

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
				.prepareStatement("UPDATE products SET name = ?, category_id = ?, release_year = ?, pg_rating = ?,"
						+ " duration = ?, rent_cost = ?, buy_cost = ?, description = ?, poster = ?, trailer = ?, writers = ?,"
						+ "actors = ?, sale_percent = ?, sale_validity = ? WHERE product_id = ?")) {
			// Non Mandatory Dates
			LocalDate saleValidity = p.getSaleValidity();
			int paramCounter = 1;
			
			ps.setString(paramCounter++, p.getName()); // Name
			ps.setInt(paramCounter++, p.getProductCategory().getId()); //Product category id
			ps.setInt(paramCounter++, p.getReleaseDate().getYear()); // Release year
			ps.setString(paramCounter++, p.getPgRating()); // PG Rating
			ps.setInt(paramCounter++, p.getDuration()); // Duration
			ps.setDouble(paramCounter++, p.getOriginalRentCost()); // Original Rent Cost
			ps.setDouble(paramCounter++, p.getOriginalBuyCost()); // Original Buy Cost
			ps.setString(paramCounter++, p.getDescription()); // Description
			ps.setString(paramCounter++, p.getPoster()); // Poster
			ps.setString(paramCounter++, p.getTrailer()); // Trailer
			ps.setString(paramCounter++, p.getWriters());// Writers
			ps.setString(paramCounter++, p.getActors());// Actors
			ps.setDouble(paramCounter++, p.getSalePercent());// Sale percent
			ps.setDate(paramCounter++, (saleValidity != null ? Date.valueOf(saleValidity) : null)); // Validity

			ps.setInt(paramCounter++, p.getId()); // Id
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
	public Product getProductById(int productId) throws SQLException, InvalidProductDataException{
		Product product = null;
		
		String sql = "SELECT  p.product_id, p.name, p.category_id, p.release_year, p.pg_rating,p.duration, p.rent_cost, " + 
				"p.buy_cost, p.description, p.poster, p.trailer, p.writers, p.actors, p.sale_percent, " + 
				"p.sale_validity, m.director, tv.season, tv.finished_airing " + 
				"FROM products AS p " + 
				"LEFT JOIN movies AS m USING (product_id) " + 
				"LEFT JOIN tvseries AS tv USING (product_id)" +
				"WHERE p.product_id = ?";
		try(PreparedStatement ps = con.prepareStatement(sql)){
			ps.setInt(1, productId);
		
			//If a product matches the criteria
			try(ResultSet rs = ps.executeQuery()){
				if(rs.next()) {
					
					Date saleValidity = rs.getDate("sale_validity");
					Date finishedAiring = rs.getDate("finished_airing");
					ProductCategory productCategory = WebSite.getProductCategoryById(rs.getInt("category_id"));
					
					//Collect the product's genres
					Set<Genre> genres = new HashSet<>(ProductDao.getInstance().getProductGenresById(productId));
					
					//Collect the product's raters
					Map<Integer, Double> raters = new TreeMap<>(ProductDao.getInstance().getProductRatersById(productId));
					
					//Create the product
					product = SimpleProductFactory.createProduct(productId, //Product id
							rs.getString(DEFAULT_FILTER_ORDERED_BY),//Product name
							productCategory,//Category id
							rs.getDate("release_year").toLocalDate(), //Release year
							rs.getString("pg_rating"), //Pg rating
							rs.getInt("duration"),//Duration
							rs.getDouble("rent_cost"),//Rent cost
							rs.getDouble("buy_cost"),//Buy cost
							rs.getString("description"),//Description
							rs.getString("poster"),//Poster
							rs.getString("trailer"),//Trailer
							rs.getString("writers"),//Writers
							rs.getString("actors"),//Actors
							genres,//Genres
							raters,//Raters
							rs.getDouble("sale_percent"), //Sale percent
							saleValidity != null ? saleValidity.toLocalDate() : null, //Sale validity
							rs.getString("director"),
							rs.getInt("season"),
							finishedAiring != null ? finishedAiring.toLocalDate() : null);
				}
			}
		}
		
		return product;
	}
	
	@Override
	public Collection<Product> getAllProducts() throws SQLException, InvalidProductDataException{
		return getProducts(null);
	}
	
	@Override
	public Collection<Product> getProducts(List<Integer> identifiers) throws SQLException, InvalidProductDataException {
		Collection<Product> allProducts = new ArrayList<>();
		
		//Building the query
		StringBuilder sql = new StringBuilder(
				"SELECT  p.product_id, p.name, p.category_id, p.release_year, p.pg_rating,p.duration, p.rent_cost, " + 
				"p.buy_cost, p.description, p.poster, p.trailer, p.writers, p.actors, p.sale_percent, " + 
				"p.sale_validity, m.director, tv.season, tv.finished_airing " + 
				"FROM products AS p " + 
				"LEFT JOIN movies AS m USING (product_id) " + 
				"LEFT JOIN tvseries AS tv USING (product_id) ");
		
		//Check if the given list is not null or empty
		if(identifiers != null && !identifiers.isEmpty()) {
			sql.append(" WHERE product_id IN(");
			Supp.inClauseAppender(sql, identifiers);
		}
		
		sql.append("ORDER BY product_id ASC;");
		
		//Product data lists
		List<String> names = new ArrayList<>();
		List<ProductCategory> categories = new ArrayList<>();
		List<LocalDate> releaseYears = new ArrayList<>();
		List<String> pgRatings = new ArrayList<>();
		List<Integer> durations = new ArrayList<>();
		List<Double> rentCosts = new ArrayList<>();
		List<Double> buyCosts = new ArrayList<>();
		List<String> descriptions = new ArrayList<>();
		List<String> posters = new ArrayList<>();
		List<String> trailers = new ArrayList<>();
		List<String> writers = new ArrayList<>();
		List<String> actors = new ArrayList<>();
		List<Double> salePercents = new ArrayList<>();
		List<LocalDate> saleValidities = new ArrayList<>();
		List<String> directors = new ArrayList<>();
		List<Integer> seasons = new ArrayList<>();
		List<LocalDate> finishedAirings = new ArrayList<>();
		

		try(PreparedStatement ps = con.prepareStatement(sql.toString())){
			//Fill the parameters if the list isn't empty or null
			int paramCounter = 1;
			
			if(identifiers != null && !identifiers.isEmpty()) {
				for (Integer id : identifiers) {
					ps.setInt(paramCounter++, id);
				}
			}
			//Reset the identifiers collection
			identifiers = new ArrayList<>();
			
			try(ResultSet rs = ps.executeQuery()){
				//Collect the data for each product in a separate list
				while(rs.next()) {
					Date saleValidity = rs.getDate("sale_validity");
					Date finishedAiring = rs.getDate("finished_airing");
					
					identifiers.add(rs.getInt("product_id"));
					names.add(rs.getString(DEFAULT_FILTER_ORDERED_BY));
					categories.add(WebSite.getProductCategoryById(rs.getInt("category_id")));
					releaseYears.add(rs.getDate("release_year").toLocalDate());
					pgRatings.add(rs.getString("pg_rating"));
					durations.add(rs.getInt("duration"));
					rentCosts.add(rs.getDouble("rent_cost"));
					buyCosts.add(rs.getDouble("buy_cost"));
					descriptions.add(rs.getString("description"));
					posters.add(rs.getString("poster"));
					trailers.add(rs.getString("trailer"));
					writers.add(rs.getString("writers"));
					actors.add(rs.getString("actors"));
					salePercents.add(rs.getDouble("sale_percent"));
					saleValidities.add(saleValidity != null ? saleValidity.toLocalDate() : null);
					directors.add(rs.getString("director"));
					seasons.add(rs.getInt("season"));
					finishedAirings.add(finishedAiring != null ? finishedAiring.toLocalDate() : null);
				}
			}
			//Collect the genres and raters
			Map<Integer, Collection<Genre>> productsGenres = getProductGenresById(identifiers);//Map<ProductId, Collection<Genre>>;
			Map<Integer, Map<Integer, Double>> productsRaters = getProductRatersById(identifiers);//Map<ProductId,Map<UserId,UserRating>>
			
			//Fill the collection of products
			for(int i = 0; i < identifiers.size(); i++ ) {
				Integer productId = identifiers.get(i);
				Set<Genre> genres = new HashSet<>(productsGenres.get(productId));
				Map<Integer, Double> raters = productsRaters.get(productId);
				
				Product product = SimpleProductFactory.createProduct(productId, //Product name
						names.get(i),
						categories.get(i),
						releaseYears.get(i),
						pgRatings.get(i),
						durations.get(i),
						rentCosts.get(i),
						buyCosts.get(i),
						descriptions.get(i),
						posters.get(i),
						trailers.get(i),
						writers.get(i),
						actors.get(i),
						genres,
						raters,
						salePercents.get(i),
						saleValidities.get(i),
						directors.get(i),
						seasons.get(i),
						finishedAirings.get(i));
				
				allProducts.add(product);
			}
		}
		return allProducts;
	}

	public Map<Integer, Collection<Genre>> getProductGenresById(List<Integer> productIdentifiers) throws SQLException{
		Map<Integer, Collection<Genre>> productGenres = new HashMap<>();
		
		//Building the query
		StringBuilder sql = new StringBuilder("SELECT product_id, genre_id FROM product_has_genres WHERE product_id IN (");
		
		//Check if there is anything to select in the first place
		if(productIdentifiers != null && !productIdentifiers.isEmpty()) {
			//Append the desired IDs in the IN clause 
			Supp.inClauseAppender(sql, productIdentifiers);
			sql.append("ORDER BY product_id ASC;");
			
			int paramCounter = 1;
			try(PreparedStatement ps = con.prepareStatement(sql.toString())){
				//Assign a parameter value for each identifier in the list
				for (Integer id : productIdentifiers) {
					ps.setInt(paramCounter++, id);
				}
				
				try(ResultSet rs = ps.executeQuery()){
					while(rs.next()) {
						//Fill the collection
						Integer productId = rs.getInt("product_id");
						Genre genre = WebSite.getGenreById(rs.getInt("genre_id"));
						
						if(!productGenres.containsKey(productId)) {
							productGenres.put(productId, new ArrayList<Genre>());
						}
						productGenres.get(productId).add(genre);		
					}
				}
			}
		}
		
		return productGenres;
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
	
	public Map<Integer, Map<Integer, Double>> getProductRatersById(List<Integer> productIdentifiers) throws SQLException{
		Map<Integer, Map<Integer, Double>> productRaters = new HashMap<>();
		
		//Building the query
		StringBuilder sql = new StringBuilder("SELECT product_id, user_id, rating FROM product_has_raters WHERE product_id IN (");
		
		//Check if there is anything to select in the first place
		if(productIdentifiers != null && !productIdentifiers.isEmpty()) {
			//Append the desired IDs in the IN clause 
			Supp.inClauseAppender(sql, productIdentifiers);
			
			int paramCounter = 1;
			try(PreparedStatement ps = con.prepareStatement(sql.toString())){
				//Assign a parameter value for each identifier in the list
				for (Integer id : productIdentifiers) {
					ps.setInt(paramCounter++, id);
				}
				
				try(ResultSet rs = ps.executeQuery()){
					while(rs.next()) {
						//Fill the collection
						Integer product_id = rs.getInt("product_id");
						Integer user_id = rs.getInt("user_id");
						Double rating = rs.getDouble("rating");
						
						if(!productRaters.containsKey(product_id)) {
							//If no such product --> add new key
							productRaters.put(product_id, new HashMap<Integer,Double>());
						}
						//Placing the rating
						productRaters.get(product_id).put(user_id, rating);
					}
				}
			}
		}
		
		return productRaters;
	}
	
	@Override
	public Map<Integer, Double> getProductRatersById(int movieId) throws SQLException {
		Map<Integer, Double> productRaters = new HashMap<>();

		try (PreparedStatement ps = con
				.prepareStatement("SELECT user_id, rating FROM product_has_raters WHERE product_id = ?");) {
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

	public Collection<Product> getProductsOnSale(Integer limit) throws SQLException, InvalidProductDataException{
		List<Integer> identifiers = new ArrayList<>();
		
		//Get the list of cheapest products
		String sql = "SELECT product_id FROM products " + 
				"	WHERE sale_validity >= curdate()";
		if(limit != null) {
			sql = sql.concat("  LIMIT ?;");
		}
		
		try(PreparedStatement st =con.prepareStatement(sql)){
			if(limit != null) {
				st.setInt(1, limit);
			}
			try(ResultSet rs = st.executeQuery()){
				while(rs.next()) {
					identifiers.add(rs.getInt("product_id"));
				}
			}
		}
		//Return the products
		return getProducts(identifiers);
	}
	
	public Collection<Product> getMostPopularProducts(Integer limit) throws SQLException, InvalidProductDataException{
		List<Integer> identifiers = new ArrayList<>();
		
		//Get the list of cheapest products
		String sql = "SELECT product_id, COUNT(product_id) AS times_bought FROM order_has_products " + 
				"	GROUP BY product_id ORDER BY times_bought DESC ";
		if(limit != null) {
			sql = sql +"  LIMIT ?;";
		}
		
		try(PreparedStatement st =con.prepareStatement(sql)){
			if(limit != null) {
				st.setInt(1, limit);
			}
			try(ResultSet rs = st.executeQuery()){
				while(rs.next()) {
					identifiers.add(rs.getInt("product_id"));
				}
			}
		}
		//Return the products
		return getProducts(identifiers);
	}
	
	public Collection<Product> getHighestRatedtProducts(Integer limit) throws SQLException, InvalidProductDataException{
		List<Integer> identifiers = new ArrayList<>();
		
		//Get the list of cheapest products
		String sql = "SELECT product_id, (SUM(rating)/COUNT(user_id)) AS rating" + 
				"	FROM product_has_raters GROUP BY product_id ORDER BY rating DESC";
		//Check if limit is ommited
		if(limit != null) {
			sql = sql +"  LIMIT ?;";
		}
		
		try(PreparedStatement st =con.prepareStatement(sql)){
			//Set limit if not ommited
			if(limit != null) {
				st.setInt(1, limit);
			}
			try(ResultSet rs = st.executeQuery()){
				while(rs.next()) {
					identifiers.add(rs.getInt("product_id"));
				}
			}
		}
		//Return the products
		return getProducts(identifiers);
	}
	
	public Collection<Product> getCheapestProducts(Integer limit) throws SQLException, InvalidProductDataException{
		List<Integer> identifiers = new ArrayList<>();
		
		//Get the list of cheapest products
		String sql = "SELECT product_id FROM products ORDER BY buy_cost ASC";
		if(limit != null) {
			sql = sql +"  LIMIT ?;";
		}
		
		try(PreparedStatement st =con.prepareStatement(sql)){
			if(limit != null) {
				st.setInt(1, limit);
			}
			try(ResultSet rs = st.executeQuery()){
				while(rs.next()) {
					identifiers.add(rs.getInt("product_id"));
				}
			}
		}
		//Return the products
		return getProducts(identifiers);
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
		List<Integer> genresIDs = new ArrayList<>();
		for (Genre genre : filter.getGenres()) {
			genresIDs.add(genre.getId());
		}
		
		
		if(genresIDs != null && !genresIDs.isEmpty()) {
			query.append("AND (g.genre_id IS NULL OR g.genre_id IN(");
			Supp.inClauseAppender(query, genresIDs);
			query.append(")");
		}
		
		//Add the ordering part (
		query.append("GROUP BY p.name ORDER BY "+filter.getOrderedBy()+" " + (filter.isAscending() ? "ASC" : "DESC"));
		
		
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
			for (Integer genre_id : genresIDs) {
				ps.setInt(paramCounter++, genre_id);
			}
			
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

	public ProductQueryInfo getFilterInfo() throws SQLException, InvalidProductQueryInfoException {
		ProductQueryInfo filter = null;
		//Create a query to select all neccessary data
		String sql = "SELECT MIN(release_year) minReleaseYear, MAX(release_year) maxReleaseYear,\r\n" + 
				"	MIN(duration) minDuration, MAX(duration) maxDuration,\r\n" + 
				"	MIN(buy_cost) minBuyCost, MAX(buy_cost) maxBuyCost,\r\n" + 
				"	MIN(rent_cost) minRentCost, MAX(rent_cost) maxRentCost FROM products;";
		
		List<Genre> genres = new ArrayList<>(WebSite.getAllGenres().values());
		
		//Create a statement
		try(Statement st = con.createStatement()){
			//Execute statement and collect data
			try(ResultSet rs = st.executeQuery(sql)){
				if(rs.next()) {
					//Return the initial filter object
					filter = new ProductQueryInfo(null, //No initial name should be given
							rs.getInt("minReleaseYear"), //Min release year
							rs.getInt("maxReleaseYear"), //Max release year
							rs.getInt("minDuration"), //Min duration
							rs.getInt("maxDuration"), // Max duration
							rs.getDouble("minBuyCost"), //Min buy cost
							rs.getDouble("maxBuyCost"),//Max buy cost
							rs.getDouble("minRentCost"), //Min rent cost
							rs.getDouble("maxRentCost"),//Max rent cost
							genres, //Genres map
							DEFAULT_FILTER_ORDERED_BY, //Default column for ordering
							true);//Default ascending order
				}
			}
		}
		return filter;
	}
}
