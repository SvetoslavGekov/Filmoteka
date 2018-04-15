package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Product;
import model.User;
import webSite.WebSite;

/**
 * Servlet implementation class LoadAllTVSeriesAsJsonServlet
 */
@WebServlet("/LoadAllTVSeriesAsJsonServlet")
public class LoadAllTVSeriesAsJsonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		GsonBuilder gBuilder = new GsonBuilder();
		gBuilder.setPrettyPrinting();
		Gson result = gBuilder.create();

		ArrayList<Product> products = new ArrayList<>(WebSite.getAllTVSeries());
		String jsonString = result.toJson(products);
		response.getWriter().write(jsonString);
	}

}
