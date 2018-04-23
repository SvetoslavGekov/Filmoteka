package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.manager.UserManager;
import exceptions.ExceptionHandler;
import exceptions.InvalidProductDataException;
import model.Product;
import model.User;
import model.dao.ProductDao;

/**
 * Servlet implementation class RemoveProductFromCartServlet
 */
@WebServlet("/auth/remcartproducts")
public class RemoveProductFromCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get user from session
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("USER");

		// Get product from website
		Integer productId = Integer.valueOf(request.getParameter("productId"));
		Product product = null;
		try {
			product = ProductDao.getInstance().getProductById(productId);
		}
		catch (SQLException | InvalidProductDataException e1) {
			ExceptionHandler.handleDatabaseProcessingException(response);
		}

		// Check if the productId is valid
		if (product != null) {
			// Remove product from shopping cart
			UserManager.getInstance().removeProductFromShoppingCart(user, product);
		}
		
		System.out.println("\nRemoved product from cart:");
		for (Entry<Product,LocalDate> e: user.getShoppingCart().entrySet()) {
			System.out.println(String.format("%s	%s", e.getKey().getName(), e.getValue()));
		}
	}

}
