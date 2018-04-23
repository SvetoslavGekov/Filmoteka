package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import exceptions.ExceptionHandler;
import exceptions.InvalidProductQueryInfoException;
import model.Product;
import model.dao.ProductDao;
import util.productFilters.ProductQueryInfo;
import util.productFilters.ProductQueryInfoDeserializer;

/**
 * Servlet implementation class FilterProductsServlet
 */
@WebServlet("/filter")
public class FilterProductsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Create a ProductQueryInfo with all neccessary data from the database
			ProductQueryInfo filter = ProductDao.getInstance().getFilterInfo();

			// Append the filter in the response as a JSON
			// Create json builder
			GsonBuilder gBuilder = new GsonBuilder();
			gBuilder.setPrettyPrinting();
			Gson result = gBuilder.serializeNulls().create();

			// JSON the filter
			String jsonString = result.toJson(filter);

			response.getWriter().write(jsonString);
		}
		catch (SQLException | InvalidProductQueryInfoException e) {
			ExceptionHandler.handleException(response, "An error occured while loading the filter information from the database."
					+ " Please try again!", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// 1. get received JSON data from request
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String json = "";
			if (br != null) {
				json = br.readLine();
			}

			// Create GSON builder
			GsonBuilder gBuilder = new GsonBuilder().setPrettyPrinting();
			gBuilder.registerTypeAdapter(ProductQueryInfo.class, new ProductQueryInfoDeserializer());
			Gson gson = gBuilder.create();

			ProductQueryInfo pqi = gson.fromJson(json, ProductQueryInfo.class);

			// Get an array list of product identifiers
			ArrayList<Integer> productIdentifiers = (ArrayList<Integer>) ProductDao.getInstance().getFilteredProducts(pqi);

			// Collect the products
			ArrayList<Product> filteredProducts = new ArrayList<>();
			for (Integer identifier : productIdentifiers) {
				filteredProducts.add(ProductDao.getInstance().getProductById(identifier));
			}
			
			// Send products over as JSON
			String jsonString = gson.toJson(filteredProducts);

			response.getWriter().write(jsonString);
		}

		catch (Exception e) {
			ExceptionHandler.handleException(response, "An error occured while loading the movies from the database."
					+ " Please try again!", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
