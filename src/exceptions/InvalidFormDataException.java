package exceptions;

public class InvalidFormDataException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -489403477629806901L;

	public InvalidFormDataException() {
	}

	public InvalidFormDataException(String message) {
		super(message);
	}

	public InvalidFormDataException(Throwable cause) {
		super(cause);
	}

	public InvalidFormDataException(String message, Throwable cause) {
		super(message, cause);
	}


}
