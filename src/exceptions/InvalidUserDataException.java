package exceptions;

public class InvalidUserDataException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3505056472780024254L;

	public InvalidUserDataException() {
		super();
	}

	public InvalidUserDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidUserDataException(String message) {
		super(message);
	}

	public InvalidUserDataException(Throwable cause) {
		super(cause);
	}


}
