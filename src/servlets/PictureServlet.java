package servlets;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PictureServlet
 */
@WebServlet("/getPic")
public class PictureServlet extends HttpServlet {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4028504418244790538L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pic = req.getParameter("pic");

		File f = new File("C:\\images\\"+pic);
		try(BufferedInputStream is = new BufferedInputStream(new FileInputStream(f));){
			OutputStream os = resp.getOutputStream();
			int b = is.read();
			while(b != -1) {
				os.write(b);
				b = is.read();
			}
		}
	}

}
