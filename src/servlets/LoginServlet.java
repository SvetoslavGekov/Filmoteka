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
import exceptions.InvalidProductDataException;
import model.Product;
import model.User;
import model.dao.ProductDao;

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
		}
		catch (InvalidProductDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
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
			request.setAttribute("error","Invalid Username or Password");
			request.getRequestDispatcher("LoginForm.jsp").forward(request, response);          
			//response.sendRedirect("InvalidLogin.html");
		}
	}

}
