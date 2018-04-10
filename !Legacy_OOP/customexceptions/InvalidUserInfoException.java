package customexceptions;

public class InvalidUserInfoException extends Exception {
	
	//Constructor
	public InvalidUserInfoException() {
		super();
	}
	
	public InvalidUserInfoException(String message) {
		super(message);
	}
	
	public InvalidUserInfoException(Throwable cause	) {
		super(cause);
	}
	
	public InvalidUserInfoException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
