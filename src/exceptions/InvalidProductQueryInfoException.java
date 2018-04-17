package exceptions;

public class InvalidProductQueryInfoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5743075455797349394L;

	public InvalidProductQueryInfoException() {
		super();
	}

	public InvalidProductQueryInfoException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidProductQueryInfoException(String message) {
		super(message);
	}

	public InvalidProductQueryInfoException(Throwable cause) {
		super(cause);
	}

}
