package model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import model.TVSeries;

public class TVSeriesDao implements ITVSeriesDao {
	// Fields
	private static TVSeriesDao instance;
	private static Connection con;

	// Constructors
	private TVSeriesDao() {
		// Create the connection object from the DBManager
		TVSeriesDao.con = DBManager.getInstance().getCon();
	}

	// Methods
	public synchronized static TVSeriesDao getInstance() {
		if (instance == null) {
			instance = new TVSeriesDao();
		}
		return instance;
	}

	@Override
	public void saveTVSeries(TVSeries tvs) throws SQLException, InvalidProductDataException {
		// TODO --> Transactions
		synchronized (con) {
			con.setAutoCommit(false);
			try {
				// Insert the movie in the products table
				ProductDao.getInstance().saveProduct(tvs);

				// Insert the movie in the movies table
				try (PreparedStatement ps = con.prepareStatement("INSERT INTO tvseries (product_id) VALUES (?);")) {
					ps.setInt(1, tvs.getId());
					ps.executeUpdate();
				}
				con.commit();
			}
			catch (SQLException e) {
				// Rollback
				con.rollback();
				throw e;
			}
			finally {
				con.setAutoCommit(true);
			}
		}
	}

	@Override
	public void updateTVSeries(TVSeries tvs) throws SQLException {
		// TODO -> transactions
		synchronized (con) {
			con.setAutoCommit(false);
			try {
				// Update the product basic information
				ProductDao.getInstance().updateProduct(tvs);

				// Update the movie specific information
				try (PreparedStatement ps = con.prepareStatement(
						"UPDATE tvseries SET season = ?, finished_airing = ? WHERE product_id = ?;")) {
					ps.setInt(1, tvs.getSeason());
					ps.setDate(2, java.sql.Date.valueOf(tvs.getFinishedAiring()));
					ps.executeUpdate();
				}
				con.commit();
			}
			catch (SQLException e) {
				// Rollback
				con.rollback();
				throw e;
			}
			finally {
				con.setAutoCommit(true);
			}
		}
	}

	@Override
	public Collection<TVSeries> getAllTVSeries() throws SQLException, InvalidProductDataException {
		Collection<TVSeries> allTVSeries = new ArrayList<TVSeries>();
		try (PreparedStatement ps = con.prepareStatement(
				"SELECT tv.season, tv.finished_airing, p.* FROM tvseries AS tv\r\n" + "	INNER JOIN products AS p USING (product_id);")) {
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					int tvsID = rs.getInt("product_id");
					// Collect the movie's genres
					Set<Genre> genres = new HashSet<>(ProductDao.getInstance().getProductGenresById(tvsID));

					// Collect the movie's raters
					Map<Integer, Double> raters = new TreeMap<>(ProductDao.getInstance().getProductRatersById(tvsID));

					// Construct the new movie
					Date finishedAiring = rs.getDate("finished_airing");
					TVSeries tvs = new TVSeries(tvsID, rs.getString("name"), rs.getDate("release_year").toLocalDate(),
							rs.getString("pg_rating"), rs.getInt("duration"), rs.getDouble("rent_cost"),
							rs.getDouble("buy_cost"), rs.getString("description"), rs.getString("poster"),
							rs.getString("trailer"), rs.getString("writers"), rs.getString("actors"), genres, raters,
							rs.getInt("season"), (finishedAiring != null) ? finishedAiring.toLocalDate() : null);

					allTVSeries.add(tvs);
				}

			}
		}
		if (allTVSeries.isEmpty()) {
			return Collections.emptyList();
		}
		return allTVSeries;
	}

}
