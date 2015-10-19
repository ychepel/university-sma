package university.domain;

public class DomainException extends Exception {
	public DomainException() {
		super();
	}
	
	public DomainException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DomainException(String message) {
		super(message);
	}
}
