package model.dao.nomenclatures;

import java.sql.SQLException;
import java.util.Map;

import exceptions.InvalidGenreDataException;
import model.nomenclatures.Genre;

public interface IGenreDao {
	
	void saveGenre(Genre g) throws SQLException, InvalidGenreDataException;
	
	void updateGenre(Genre g) throws SQLException;
	
	Map<Integer,Genre> getAllGenres() throws SQLException, InvalidGenreDataException;
}
