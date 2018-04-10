package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get current session
		HttpSession session = request.getSession();
		//Get saved user
		User user = (User) session.getAttribute("USER");
		
		if(user.getUsername() != null) {
			response.setContentType("text/html");
			ServletOutputStream sos = response.getOutputStream();
			sos.println("<html><body><h1>");
			sos.println(String.format("Hello %s	Email: %s", user.getUsername(), user.getEmail()));
			sos.println("</h1></body></html>");
		}
		else {
			response.sendRedirect("LoginForm.html");
		}
	}

}
