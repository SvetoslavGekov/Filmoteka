package model.dao;

import java.sql.SQLException;
import java.util.Collection;

import exceptions.InvalidProductDataException;
import model.TVSeries;

public interface ITVSeriesDao {
	void saveTVSeries(TVSeries tvs) throws SQLException, InvalidProductDataException;
	
	void updateTVSeries(TVSeries tvs) throws SQLException;
	
	Collection<TVSeries> getAllTVSeries() throws SQLException, InvalidProductDataException;
}
