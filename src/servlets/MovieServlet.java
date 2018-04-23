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

import controller.manager.MovieManager;
import exceptions.ExceptionHandler;
import exceptions.InvalidGenreDataException;
import exceptions.InvalidProductDataException;
import model.Movie;
import model.Product;
import model.dao.ProductDao;
import model.nomenclatures.Genre;
import util.WebSite;

/**
 * Servlet implementation class CreateMovieServlet
 */
@WebServlet("/adm/movies")
public class MovieServlet extends HttpServlet {
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
			Integer salePercent = !salePercentString.isEmpty() ? Integer.parseInt(salePercentString):0;
			
			String saleValidityString = request.getParameter("saleValidity").trim();
			LocalDate saleValidity = !saleValidityString.isEmpty() ? LocalDate.parse(saleValidityString) : null;
			
			String director = request.getParameter("director").trim();
			String genresString = request.getParameter("genres").trim();
			
			Set<Genre> genres = WebSite.getGenresFromString(genresString);
			
			//Create new movie
			
			MovieManager.getInstance().createNewMovie(name, categoryId, releaseDate, pgRating, duration, rentCost,
					buyCost, description, poster, trailer, writers, actors, genres, salePercent, saleValidity, director);
		}
		catch(NumberFormatException | DateTimeParseException e) {
			//Tell the user that there was something wrong with the input
			ExceptionHandler.handleException(response, "Sorry, an error occured while reading your input data. Please follow the hints in the form"
					+ "and try again!", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		catch (InvalidProductDataException | InvalidGenreDataException e ) {
			//Tell the user that there was an error with creating the product 
			ExceptionHandler.handleException(response, "Sorry, an error occured while creating your movie. The error was " +e.getMessage(),
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		catch (SQLException e) {
			//Tell the user that there was an issue with the database
			ExceptionHandler.handleException(response, "Sorry, an error occured while saving your movie to the database. Please try agian!",
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
			Integer salePercent = !salePercentString.isEmpty() ? Integer.parseInt(salePercentString):0;
			
			String saleValidityString = request.getParameter("saleValidity").trim();
			LocalDate saleValidity = !saleValidityString.isEmpty() ? LocalDate.parse(saleValidityString) : null;
			
			String director = request.getParameter("director").trim();
			String genresString = request.getParameter("genres").trim();
			
			Set<Genre> genres = WebSite.getGenresFromString(genresString);
			
			//Get the existing movie
			Integer movieId = Integer.parseInt(request.getParameter("productId").trim());
			Product m = ProductDao.getInstance().getProductById(movieId);
			
			if(m != null && m instanceof Movie) {
				//Update the movie's information
				MovieManager.getInstance().updateMovie(m.getId(), name, categoryId, releaseDate, pgRating, 
						duration, rentCost, buyCost, description, poster, trailer, writers, actors, genres,
						salePercent, saleValidity, director);
			}
		}
		catch(NumberFormatException | DateTimeParseException e) {
			//Tell the user that there was something wrong with the input
			ExceptionHandler.handleException(response, "Sorry, an error occured while reading your input data. Please follow the hints in the form"
					+ "and try again!", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		catch (InvalidProductDataException | InvalidGenreDataException e ) {
			//Tell the user that there was an error with creating the product 
			ExceptionHandler.handleException(response, "Sorry, an error occured while updating your movie's information."
					+ " The error was " +e.getMessage(),
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		catch (SQLException e) {
			//Tell the user that there was an issue with the database
			ExceptionHandler.handleException(response, "Sorry, an error occured while updating your movie in the database. Please try agian!",
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
