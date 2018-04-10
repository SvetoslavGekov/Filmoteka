package model.dao;

import java.sql.SQLException;
import java.util.Collection;

import exceptions.InvalidProductDataException;
import model.Movie;

public interface IMovieDao {
	void saveMovie(Movie m) throws SQLException, InvalidProductDataException;
	
	void updateMovie(Movie m) throws SQLException;
	
	Collection<Movie> getAllMovies() throws SQLException, InvalidProductDataException;
}
