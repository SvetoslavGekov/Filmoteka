package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import exceptions.InvalidProductDataException;
import model.Product;
import model.dao.ProductDao;

/**
 * Servlet implementation class LoadAllProductsAsJsonServlet
 */
@WebServlet("/LoadAllProductsAsJsonServlet")
public class LoadAllProductsAsJsonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ArrayList<Product> products;
		try {
			GsonBuilder gBuilder = new GsonBuilder();
			gBuilder.setPrettyPrinting();
			Gson result = gBuilder.create();

			products = new ArrayList<>(ProductDao.getInstance().getProducts(null));

			String jsonString = result.toJson(products);
			response.getWriter().write(jsonString);
		}
		catch (SQLException | InvalidProductDataException e) {
			// TODO Handle exception with AJAX
			e.printStackTrace();
		}

	}

}
