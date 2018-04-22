package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import exceptions.InvalidProductDataException;
import model.Product;
import model.User;
import model.dao.ProductDao;

@WebServlet("/auth/account")
public class MyAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("USER");
		
		if(user != null){
			try {
				GsonBuilder gBuilder = new GsonBuilder();
				gBuilder.setPrettyPrinting();
				Gson result = gBuilder.create();

				String jsonString = result.toJson(user);
				response.getWriter().write(jsonString);
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		else{
			request.getRequestDispatcher("LoginForm.jsp").forward(request, response);
		}
		
	}
}
