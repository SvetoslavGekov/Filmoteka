package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.manager.MovieManager;
import exceptions.InvalidProductDataException;
import model.nomenclatures.Genre;

/**
 * Servlet implementation class CreateMovieServlet
 */
@WebServlet("/movies")
public class CreateMovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		//Get the parameters from the login form
		//Mandatory fields
		//TODO -> grab entire form as JSON (remove parsing issues)
		Integer categoryId = Integer.parseInt(request.getParameter("categoryId"));
		String name = request.getParameter("name");
		LocalDate releaseDate = LocalDate.parse(request.getParameter("releaseYear"));
		String pgRating = request.getParameter("pgRating");
		Integer duration = Integer.parseInt(request.getParameter("duration"));
		Double rentCost = Double.valueOf(request.getParameter("rentCost"));
		Double buyCost = Double.valueOf(request.getParameter("buyCost"));
		
		//Optional fields
		String description = request.getParameter("description");
		String poster = request.getParameter("poster");
		String trailer = request.getParameter("trailer");
		String writers = request.getParameter("writers");
		String actors = request.getParameter("actors");
		Double salePercent = Double.valueOf(request.getParameter("salePercent"));
		LocalDate saleValidity = LocalDate.parse(request.getParameter("saleValidity"));
		String director = request.getParameter("director");
		//TODO -> grab genres
		HashSet<Genre> genres = new HashSet<>();
		
		try {
			//Create new movie
			
			MovieManager.getInstance().createNewMovie(name, categoryId, releaseDate, pgRating, duration, rentCost,
					buyCost, description, poster, trailer, writers, actors, genres, salePercent, saleValidity, director);
			//Set response
			//TODO -> Set proper response
		}
		catch (InvalidProductDataException e ) {
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("Error.jsp").forward(request, response);
		}
		catch (SQLException e) {
			request.setAttribute("error", "We appear to be having some issues with our database, please try again later.");
			request.getRequestDispatcher("Error.jsp").forward(request, response);
		}
	}

}
