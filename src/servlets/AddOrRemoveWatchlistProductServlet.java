package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.manager.UserManager;
import model.Product;
import model.User;
import webSite.WebSite;

/**
 * Servlet implementation class AddOrRemoveWatchlistProductServlet
 */
@WebServlet("/auth/watchlistproducts")
public class AddOrRemoveWatchlistProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Get user from session
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("USER");

		// Get product from website
		Integer productId = Integer.valueOf(request.getParameter("productId"));
		Product product = WebSite.getProductById(productId);

		// Check if productId is valid
		if (product != null) {
			// Add or remove product from favorites
			UserManager.getInstance().addOrRemoveProductFromWatchlist(user, product);
		}
	}

}
