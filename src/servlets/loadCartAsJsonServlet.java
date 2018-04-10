package servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import model.Product;
import model.User;

/**
 * Servlet implementation class loadCartAsJsonServlet
 */
@WebServlet("/loadCartAsJsonServlet")
public class loadCartAsJsonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get user from session
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("USER");
		
		//Create json builder
		GsonBuilder gBuilder = new GsonBuilder().enableComplexMapKeySerialization();
		gBuilder.setPrettyPrinting();
		Gson result = gBuilder.create();

		//Get user's cart
		Map<Product,LocalDate> cart = new HashMap<>(user.getShoppingCart());
		String jsonString = result.toJson(cart);
		
		
		response.getWriter().write(jsonString);
	}

}
