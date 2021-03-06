package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.manager.TVSeriesManager;
import exceptions.ExceptionHandler;
import exceptions.InvalidGenreDataException;
import exceptions.InvalidProductDataException;
import model.Product;
import model.TVSeries;
import model.dao.ProductDao;
import model.nomenclatures.Genre;
import util.WebSite;

/**
 * Servlet implementation class CreateTVSeriesServlet
 */
@WebServlet("/adm/tvseries")
public class TVSeriesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		try {
			//Get the parameters from the login form
			//Mandatory fields
			//TODO -> grab entire form as JSON (remove parsing issues)
			Integer categoryId = Integer.parseInt(request.getParameter("categoryId").trim());
			String name = request.getParameter("name").trim();
			LocalDate releaseDate = LocalDate.parse(request.getParameter("releaseYear").trim());
			String pgRating = request.getParameter("pgRating").trim();
			Integer duration = Integer.parseInt(request.getParameter("duration").trim());
			Double rentCost = Double.valueOf(request.getParameter("rentCost").trim());
			Double buyCost = Double.valueOf(request.getParameter("buyCost").trim());
			
			//Optional fields
			String description = request.getParameter("description").trim();
			String poster = request.getParameter("poster").trim();
			String trailer = request.getParameter("trailer").trim();
			String writers = request.getParameter("writers").trim();
			String actors = request.getParameter("actors").trim();
			
			String salePercentString = request.getParameter("salePercent").trim();
			Integer salePercent = !salePercentString.isEmpty() ? Integer.parseInt(salePercentString) : 0;
			
			String saleValidityString = request.getParameter("saleValidity").trim();
			LocalDate saleValidity = !saleValidityString.isEmpty() ? LocalDate.parse(saleValidityString) : null;
			
			String seasonString = request.getParameter("season").trim();
			Integer season = !seasonString.isEmpty() ? Integer.parseInt(seasonString) : 0;
			
			String finishedAiringString = request.getParameter("finishedAiring").trim();
			LocalDate finishedAiring = !finishedAiringString.isEmpty() ? LocalDate.parse(finishedAiringString) : null;
			
			String genresString = request.getParameter("genres").trim();
			
			Set<Genre> genres = WebSite.getGenresFromString(genresString);
			
			//Create new tv series
			
			TVSeriesManager.getInstance().createNewTVSeries(name, categoryId, releaseDate, pgRating, duration,
					rentCost, buyCost, description, poster, trailer, writers, actors, genres,
					salePercent, saleValidity, season, finishedAiring);
		}
		catch(NumberFormatException | DateTimeParseException e) {
			//Tell the user that there was something wrong with the input
			ExceptionHandler.handleException(response, "Sorry, an error occured while reading your input data. Please follow the hints in the form"
					+ "and try again!", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		catch (InvalidProductDataException | InvalidGenreDataException e ) {
			//Tell the user that there was an error with creating the product 
			ExceptionHandler.handleException(response, "Sorry, an error occured while creating your tv series. The error was " +e.getMessage(),
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		catch (SQLException e) {
			//Tell the user that there was an issue with the database
			ExceptionHandler.handleException(response, "Sorry, an error occured while saving your tv series to the database. Please try agian!",
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		try {
			//Get the parameters from the login form
			//Mandatory fields
			Integer categoryId = Integer.parseInt(request.getParameter("categoryId").trim());
			String name = request.getParameter("name").trim();
			LocalDate releaseDate = LocalDate.parse(request.getParameter("releaseYear").trim());
			String pgRating = request.getParameter("pgRating").trim();
			Integer duration = Integer.parseInt(request.getParameter("duration").trim());
			Double rentCost = Double.valueOf(request.getParameter("rentCost").trim());
			Double buyCost = Double.valueOf(request.getParameter("buyCost").trim());
			
			//Optional fields
			String description = request.getParameter("description").trim();
			String poster = request.getParameter("poster").trim();
			String trailer = request.getParameter("trailer").trim();
			String writers = request.getParameter("writers").trim();
			String actors = request.getParameter("actors").trim();
			
			String salePercentString = request.getParameter("salePercent").trim();
			Integer salePercent = !salePercentString.isEmpty() ? Integer.parseInt(salePercentString) : 0;
			
			String saleValidityString = request.getParameter("saleValidity").trim();
			LocalDate saleValidity = !saleValidityString.isEmpty() ? LocalDate.parse(saleValidityString) : null;
			
			String seasonString = request.getParameter("season").trim();
			Integer season = !seasonString.isEmpty() ? Integer.parseInt(seasonString) : 0;
			
			String finishedAiringString = request.getParameter("finishedAiring").trim();
			LocalDate finishedAiring = !finishedAiringString.isEmpty() ? LocalDate.parse(finishedAiringString) : null;
			
			String genresString = request.getParameter("genres").trim();
			
			Set<Genre> genres = WebSite.getGenresFromString(genresString);
			
			//Get the existing tv series
			Integer tvsId = Integer.parseInt(request.getParameter("productId").trim());
			Product tvs = ProductDao.getInstance().getProductById(tvsId);
			
			if(tvs != null && tvs instanceof TVSeries) {
				//Update the movie's information
				TVSeriesManager.getInstance().updateTVSeries(tvs.getId(), name, categoryId, releaseDate, pgRating, duration,
						rentCost, buyCost, description, poster, trailer, writers, actors, genres, salePercent,
						saleValidity, season, finishedAiring);
			}
		}
		catch(NumberFormatException | DateTimeParseException e) {
			//Tell the user that there was something wrong with the input
			ExceptionHandler.handleException(response, "Sorry, an error occured while reading your input data. Please follow the hints in the form"
					+ "and try again!", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		catch (InvalidProductDataException | InvalidGenreDataException e ) {
			//Tell the user that there was an error with creating the product 
			ExceptionHandler.handleException(response, "Sorry, an error occured while updating your tv series information."
					+ " The error was " +e.getMessage(),
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		catch (SQLException e) {
			//Tell the user that there was an issue with the database
			ExceptionHandler.handleException(response, "Sorry, an error occured while updating your tv series"
					+ " in the database. Please try agian!",
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
