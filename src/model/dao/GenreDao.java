package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import dbManager.DBManager;
import exceptions.InvalidGenreDataException;
import model.Genre;

public final class GenreDao implements IGenreDao {
	//Fields
	private static GenreDao instance;
	private static Connection con;

	//Constructor
	private GenreDao() {
		//Create the connection object from the DBManager
		GenreDao.con = DBManager.getInstance().getCon();
	}
	
	//Methods
	public synchronized static GenreDao getInstance() {
		if(instance == null) {
			instance = new GenreDao();
		}
		return instance;
	}

	@Override
	public void saveGenre(Genre g) throws SQLException, InvalidGenreDataException {
		try(PreparedStatement ps = con.prepareStatement("INSERT INTO genres (value) VALUES(?);", PreparedStatement.RETURN_GENERATED_KEYS)){
			ps.setString(1, g.getValue());
			//If the insertion is successful
			if(ps.executeUpdate() > 0) {
				//Update the genre's Id
				try(ResultSet rs = ps.getGeneratedKeys()){
					rs.next();
					g.setId(rs.getInt("GENERATED_KEY"));
				}
			}
		}
	}

	@Override
	public void updateGenre(Genre g) throws SQLException {
		try(PreparedStatement ps = con.prepareStatement("UPDATE genres SET value = ? WHERE genre_id = ?;")){
			ps.setString(1, g.getValue());
			ps.setInt(2, g.getId());
			ps.executeUpdate();
		}
	}

	@Override
	public Map<Integer,Genre> getAllGenres() throws SQLException, InvalidGenreDataException {
		TreeMap<Integer, Genre> allGenres = new TreeMap<Integer,Genre>();
		try(PreparedStatement ps = con.prepareStatement("SELECT genre_id, value FROM genres ORDER BY genre_id;");){
			try(ResultSet rs = ps.executeQuery();){
				//While there are genres to be created
				while(rs.next()) {
					//Create next genre with full data
					Genre g = new Genre(rs.getInt("genre_id"), rs.getString("value"));
					allGenres.put(g.getId(),g);
				}
			}
		}
		if(allGenres.isEmpty()) {
			return Collections.emptyMap();
		}
		return allGenres;
	}
}
