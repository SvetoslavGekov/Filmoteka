package servlets;

import java.io.IOException;
import java.sql.SQLException;

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
 * Servlet implementation class AddOrRemoveFavoriteProductServlet
 */
@WebServlet("/auth/favproducts")
public class AddOrRemoveFavoriteProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// Get user from session
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("USER");

			// Get product from website
			Integer productId = Integer.valueOf(request.getParameter("productId"));
			Product product = ProductDao.getInstance().getProductById(productId);
			
			// Check if the productId is valid
			if (product != null) {
				// Add or remove product from favorites
				UserManager.getInstance().addOrRemoveProductFromFavorites(user, product);
			}
		}
		catch (SQLException | InvalidProductDataException e) {
			//Tell user that an error occured while fetching the product from the database
			ExceptionHandler.handleDatabaseProcessingException(response);
		}


	}

}
