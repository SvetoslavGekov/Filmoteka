package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import dbManager.DBManager;
import exceptions.InvalidProductDataException;
import model.Genre;
import model.Movie;

public class MovieDao implements IMovieDao {
	//Fields
	private static MovieDao instance;
	private static Connection con;
	
	//Constructors
	private MovieDao() {
		//Create the connection object from the DBManager
		MovieDao.con = DBManager.getInstance().getCon();
	}
	
	//Methods
	public synchronized static MovieDao getInstance() {
		if(instance == null) {
			instance = new MovieDao();
		}
		return instance;
	}

	@Override
	//Save a newly created movie with the basic mandatory fields
	public void saveMovie(Movie m) throws SQLException, InvalidProductDataException {
		//TODO --> Transactions
		synchronized (con) {
			con.setAutoCommit(false);
			try {
				//Insert the movie in the products table
				ProductDao.getInstance().saveProduct(m);
				
				//Insert the movie in the movies table
				try(PreparedStatement ps = con.prepareStatement("INSERT INTO movies (product_id) VALUES (?);")){
					ps.setInt(1, m.getId());
					ps.executeUpdate();
				}
				con.commit();
			}
			catch(SQLException e) {
				//Rollback 
				con.rollback();
				throw e;
			}
			finally {
				con.setAutoCommit(true);
			}
		}
	}

	@Override
	public void updateMovie(Movie m) throws SQLException {
		//TODO -> transactions
		synchronized (con) {
			con.setAutoCommit(false);
			try {
				//Update the product basic information
				ProductDao.getInstance().updateProduct(m);
				
				//Update the movie specific information
				try(PreparedStatement ps = con.prepareStatement("UPDATE movies SET director = ? WHERE product_id = ?;")){
					ps.setString(1, m.getDirector());
					ps.setInt(2, m.getId());
					ps.executeUpdate();
				}
				con.commit();
			}
			catch (SQLException e) {
				//Rollback
				con.rollback();
				throw e;
			}
			finally {
				con.setAutoCommit(true);
			}
		}
		
	}

	@Override
	public Collection<Movie> getAllMovies() throws SQLException, InvalidProductDataException {
		Collection<Movie> allMovies = new ArrayList<Movie>();
		try(PreparedStatement ps = con.prepareStatement("SELECT m.director, p.* FROM movies AS m\r\n" + 
				"	INNER JOIN products AS p USING (product_id);")){
			try(ResultSet rs = ps.executeQuery();){
				while(rs.next()) {
					int movieId = rs.getInt("product_id");
					//Collect the movie's genres
					Set<Genre> genres = new HashSet<>(ProductDao.getInstance().getProductGenresById(movieId));
					
					//Collect the movie's raters
					Map<Integer, Double> raters = new TreeMap<>(ProductDao.getInstance().getProductRatersById(movieId));
					
					//Construct the new movie
					Movie m = new Movie(movieId,
							rs.getString("name"),
							rs.getDate("release_year").toLocalDate(),
							rs.getString("pg_rating"),
							rs.getInt("duration"),
							rs.getDouble("rent_cost"),
							rs.getDouble("buy_cost"),
							rs.getString("description"),
							rs.getString("poster"),
							rs.getString("trailer"),
							rs.getString("writers"),
							rs.getString("actors"),
							genres,
							raters,
							rs.getString("director"));
					
					allMovies.add(m);
				}
				
			}
		}
		if(allMovies.isEmpty()) {
			return Collections.emptyList();
		}
		return allMovies;
	}
}
