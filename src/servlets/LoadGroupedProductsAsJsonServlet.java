package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import exceptions.ExceptionHandler;
import exceptions.InvalidProductDataException;
import model.Product;
import model.dao.ProductDao;

/**
 * Servlet implementation class LoadGroupedProductsAsJsonServlet
 */
@WebServlet("/loadGrpPrd")
public class LoadGroupedProductsAsJsonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
  

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			//Create a map of: category --> list of products
			Map<String,List<Product>> groupedProducts = new TreeMap<>();
			//Get top 5 of
			groupedProducts.put("On Sale", (List<Product>) ProductDao.getInstance().getProductsOnSale(new Integer(5))); //On sale
			groupedProducts.put("Most Popular", (List<Product>) ProductDao.getInstance().getMostPopularProducts(new Integer(5))); //Most Popular
			groupedProducts.put("Highest Rated", (List<Product>) ProductDao.getInstance().getHighestRatedProducts(new Integer(5))); //Highest rated
			groupedProducts.put("Cheapest", (List<Product>) ProductDao.getInstance().getCheapestProducts(new Integer(5))); //Cheapest
			
			//Turn map into JSON
			//Create json builder
			GsonBuilder gBuilder = new GsonBuilder().enableComplexMapKeySerialization();
			gBuilder.setPrettyPrinting();
			Gson result = gBuilder.create();

			//JSON the grouped products
			String jsonString = result.toJson(groupedProducts);
			
			response.getWriter().write(jsonString);
		}
		catch (SQLException |InvalidProductDataException e) {
			ExceptionHandler.handleException(response, "An error occured while loading the movies from the database. Please try again!", 
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
