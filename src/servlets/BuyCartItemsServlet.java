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
import exceptions.InvalidOrderDataException;
import model.Order;
import model.User;

/**
 * Servlet implementation class BuyCartItemsServlet
 */
@WebServlet("/auth/buycart")
public class BuyCartItemsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get user from session
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("USER");
		
		//Attempt to buy products in cart
		try {
			UserManager.getInstance().buyProductsInCart(user);
		}
		catch (SQLException e) {
			//Tell user that an error occured while processing his request
			ExceptionHandler.handleDatabaseProcessingException(response);
		}
		catch (InvalidOrderDataException e) {
			//Tell user that an error occured while creating his order
			ExceptionHandler.handleException(response, "Sorry, an error occured while creating your order. Please try again!",
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		for (Order order : user.getOrdersHistory()) {
			System.out.printf("Id:%d	User:%d	Date:%s	Price:%.2f%n",order.getId(), order.getUserId(),
					order.getDate(), order.getTotalCost());
		}
	}
}
