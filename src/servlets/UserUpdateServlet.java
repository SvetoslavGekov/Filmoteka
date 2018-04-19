package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import exceptions.InvalidUserDataException;
import model.User;
import model.dao.UserDao;

/**
 * Servlet implementation class UpdateUserServlet
 */
@WebServlet("/updateProfile")
public class UserUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
		
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("USER");
		
		String firstName = request.getParameter("firstname").isEmpty() ? user.getFirstName() : request.getParameter("firstname");
		String lastName = request.getParameter("lastname").isEmpty() ? user.getLastName() : request.getParameter("lastname");
		String email = request.getParameter("email").isEmpty() ? user.getEmail() : request.getParameter("email");
		String phone = request.getParameter("phone").isEmpty() ? user.getPhone(): request.getParameter("phone");
		String currentPass = request.getParameter("currentPass").isEmpty() ? user.getPassword(): request.getParameter("currentPass");
		String newPass1 = request.getParameter("newPass1").isEmpty() ? user.getPassword(): request.getParameter("newPass1");
		String newPass2 = request.getParameter("newPass2").isEmpty() ? user.getPassword(): request.getParameter("newPass2");
		
		try {
			//TODO check the currnetPassword is quals to user.getPassword() (hash)
			//check if newPass1 equals newPass2 nd only then change the password
			
			//Test if can create user with these data (Will throw an exception if cannot)
			User test = new User(firstName, lastName, user.getUsername(), user.getPassword() , email);
			test.setPhone(phone);
			
			//Updating current user
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmail(email);
			user.setPhone(phone);
			UserDao.getInstance().updateUser(user);
			
			//Show an alert for sucessfully updated profile
			response.getWriter().println("<script type=\"text/javascript\">");
			response.getWriter().println("alert('Successfully updated profile!');");
			//Redirect to MyAccount.jsp
			response.getWriter().println("location='MyAccount.jsp';");
			response.getWriter().println("</script>");
			
		} catch (InvalidUserDataException | SQLException e) {
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("Error.jsp").forward(request, response);
		}
	}
}
