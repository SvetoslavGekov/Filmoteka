package exceptions;

public final class InvalidProductDataException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3404044238345770271L;

	public InvalidProductDataException() {
		super();
	};
	
	public InvalidProductDataException(String message) {
		super(message);
	}
	
	public InvalidProductDataException(Throwable cause) {
		super(cause);
	}
	
	public InvalidProductDataException(String message, Throwable cause) {
		super(message, cause);
	}
}
