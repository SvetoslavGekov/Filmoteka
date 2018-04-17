package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class EditProfileServlet
 */
@WebServlet("/editProfile")
public class EditProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	HttpSession session = request.getSession();
    	
    	if(session.getAttribute("USER") != null){
    		request.getRequestDispatcher("EditProfile.jsp").forward(request, response);
    	}
    	else{
    		request.getRequestDispatcher("LoginForm.jsp").forward(request, response);
    	}
	} 

}
