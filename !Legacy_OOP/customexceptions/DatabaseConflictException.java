package customexceptions;

public class DatabaseConflictException extends Exception {

	//Constructor
	public DatabaseConflictException() {
		super();
	}
	
	public DatabaseConflictException(String message) {
		super(message);
	}
	
	public DatabaseConflictException(Throwable cause	) {
		super(cause);
	}
	
	public DatabaseConflictException(String message, Throwable cause) {
		super(message, cause);
	}

}
