package exceptions;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class ExceptionHandler {
	//Fields
	
	//Constructor
	private ExceptionHandler() {
		
	}
	
	//Method for handling exceptions
	public static final void handleException(HttpServletResponse response, String message, int statusCode) throws IOException {
		//Write the message in the response
		response.getWriter().write(message);
		//Set the response status code
		response.setStatus(statusCode);
	}
	
	public static final void handleDatabaseProcessingException(HttpServletResponse response) throws IOException {
		//Tell user that an error occured while fetching the product from the database
		handleException(response, "Sorry, an error occured while processing your request. Please try again!",
				HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}
}
