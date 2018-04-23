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
import exceptions.InvalidProductDataException;
import exceptions.InvalidUserDataException;
import model.User;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get the parameters from the login form
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		//Check if the credentials are valid
		User user = null;
		try {
			user = UserManager.getInstance().logIn(username, password);
			
			if(user != null) {
				//Get a new session
				HttpSession session = request.getSession();
				
				//Set the user in the session
				session.setAttribute("USER", user);
				
				//Set the IP of the request which called the server
				session.setAttribute("ip", request.getRemoteAddr());
				
				//Redirect to the main_servlet
				response.sendRedirect("MainPage.html");
			}
			else {
				String message = "Invalid Username or Password";
				ExceptionHandler.handleException(response, message, HttpServletResponse.SC_UNAUTHORIZED);
			}
		} 
		catch (SQLException | InvalidProductDataException | InvalidUserDataException | InvalidOrderDataException e) {
			ExceptionHandler.handleException(response, "Sorry, an database error occured while attempting to log you in. Please try again later!",
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

	}

}
