package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.manager.UserManager;
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
		User user = UserManager.getInstance().logIn(username, password); 
		if(user != null) {
			//Get a new session
			HttpSession session = request.getSession();
			//Set the user in the session
			session.setAttribute("USER", user);
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
