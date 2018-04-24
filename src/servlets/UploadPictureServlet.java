package servlets;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class UploadPictureServlet
 */
@WebServlet("/UploadPictureServlet")
@MultipartConfig
public class UploadPictureServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get the picture as a part object
		Part filePart = request.getPart("file");
		
		//Read supplementing data
		String picture = request.getParameter("picture");
		
		//Create a new file with the given location
		File file = new File("C:\\images\\"+picture);
		
		//Get the input stream of the picture
		InputStream is = filePart.getInputStream();
		
		//Create a new output stream to write the file
		BufferedOutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(file));
			int b = is.read();
			while(b != -1) {
				os.write(b);
				b = is.read();
			}
		}
		finally {
			if(os != null) {
				os.close();
			}
		}
	}
}
