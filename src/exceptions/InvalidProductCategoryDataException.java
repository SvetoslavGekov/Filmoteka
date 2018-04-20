package exceptions;

public class InvalidProductCategoryDataException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3896358022572679793L;

	public InvalidProductCategoryDataException() {
	}

	public InvalidProductCategoryDataException(String message) {
		super(message);
	}

	public InvalidProductCategoryDataException(Throwable cause) {
		super(cause);
	}

	public InvalidProductCategoryDataException(String message, Throwable cause) {
		super(message, cause);
	}

}
