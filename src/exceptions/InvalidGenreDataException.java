package exceptions;

public class InvalidGenreDataException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4413002346710081398L;

	public InvalidGenreDataException() {
	}

	public InvalidGenreDataException(String message) {
		super(message);
	}

	public InvalidGenreDataException(Throwable cause) {
		super(cause);
	}

	public InvalidGenreDataException(String message, Throwable cause) {
		super(message, cause);
	}

}
