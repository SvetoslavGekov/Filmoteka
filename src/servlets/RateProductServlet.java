package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import exceptions.InvalidProductDataException;
import model.Product;
import model.User;
import model.dao.ProductDao;
import util.WebSite;

/**
 * Servlet implementation class RateProductServlet
 */
@WebServlet("/auth/rateproduct")
public class RateProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    @Override
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
			request.setAttribute("error", e1.getMessage());
			request.getRequestDispatcher("Error.jsp").forward(request, response);
		}
		
		//Get rating parameter
		Integer rating = Integer.valueOf(request.getParameter("rating"));
		
		// Check if the productId is valid
		if (product != null) {
			// Rate product and save in DB
			try {
				ProductDao.getInstance().rateProduct(user, product, rating);
			} catch (SQLException e) {
				request.setAttribute("error", e.getMessage());
				request.getRequestDispatcher("Error.jsp").forward(request, response);
			}
		}
		System.out.println("\nRated product from "+user.getFirstName()+" with rate =" + rating);    }

}
