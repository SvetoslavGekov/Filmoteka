package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.manager.UserManager;
import exceptions.InvalidFormDataException;
import exceptions.InvalidUserDataException;

/**
 * Servlet implementation class UserRegisterServlet
 */
@WebServlet("/UserRegisterServlet")
public class UserRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get the parameters from the login form
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		
		try {
			//Check if the given user parameters are valid
			if(!UserManager.getInstance().isValidUserRegistrationData(username, password, email)) {
				throw new InvalidFormDataException("You've entered incorrect registration data in your form. Please follow the input hints.");
			}
			
			//Check if there are no users with the same username or email
			if(UserManager.getInstance().hasUserWithSameCredentials(username, email)) {
				//TODO --> make this into AJAX and check for username and email separately
				request.setAttribute("error", "The selected username or email is already taken by another user.");
				request.getRequestDispatcher("Error.jsp").forward(request, response);
				return;
			}
			//Register user
			UserManager.getInstance().register(firstName, lastName, username, password, email);
			//Redirect to the login form again
			response.sendRedirect("LoginForm.jsp");
		}
		catch (InvalidFormDataException | InvalidUserDataException e ) {
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("Error.jsp").forward(request, response);
		}
		catch (SQLException e) {
			request.setAttribute("error", "We appear to be having some issues with our database, please try again later.");
			request.getRequestDispatcher("Error.jsp").forward(request, response);
		}
	}

}
