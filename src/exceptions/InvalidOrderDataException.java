package exceptions;

public class InvalidOrderDataException extends Exception {

	private static final long serialVersionUID = 993088612306865510L;

	public InvalidOrderDataException() {
	}

	public InvalidOrderDataException(String message) {
		super(message);
	}

	public InvalidOrderDataException(Throwable cause) {
		super(cause);
	}

	public InvalidOrderDataException(String message, Throwable cause) {
		super(message, cause);
	}


}
