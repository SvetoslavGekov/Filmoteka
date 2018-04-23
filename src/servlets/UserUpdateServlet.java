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
import validation.BCrypt;

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
		String currentPass = request.getParameter("currentPass");
		String newPass1 = request.getParameter("newPass1");
		String newPass2 = request.getParameter("newPass2");
		
		try {
			//Hash entered current pass so can be compared with user's from DB 
			currentPass = BCrypt.hashpw(currentPass, BCrypt.gensalt());
			//If all three password fields are empty set all of them as the users's password
			if(currentPass.isEmpty() && newPass1.isEmpty() && newPass2.isEmpty()){
				currentPass = user.getPassword();
				newPass1 = currentPass;
				newPass2 = currentPass;
			}
			//Otherwise check if current coincides with users's
			else if(currentPass.equals(user.getPassword())){
				//Check if newPass1 isn't equals to newPass2
				if(!newPass1.equals(newPass2)){
					throw new InvalidUserDataException("New passwords didn't match!");
				}
				
				//If everything has passed successfully, hash the new password to be set to the user
				newPass1 = BCrypt.hashpw(newPass1, BCrypt.gensalt());
			}
			else{
				throw new InvalidUserDataException("This is not your current password!");
			}
			
			//Test if can create user with these data (Will throw an exception if cannot)
			User test = new User(firstName, lastName, user.getUsername(), newPass1 , email);
			test.setPhone(phone);
			
			//Update current user
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmail(email);
			user.setPhone(phone);
			user.setPassword(newPass1);
			UserDao.getInstance().updateUser(user);
			
			//Show an alert for successfully updated profile
			response.getWriter().println("<script type=\"text/javascript\">");
			response.getWriter().println("alert('Successfully updated profile!');");
			//Redirect to MyAccount.jsp
			response.getWriter().println("location='MainPage.html';");
			response.getWriter().println("</script>");
			
		} catch (InvalidUserDataException | SQLException e) {
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("Error.jsp").forward(request, response);
		}
	}
}
