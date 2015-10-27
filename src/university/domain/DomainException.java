package university.domain;

public class DomainException extends Exception {
	private static final long serialVersionUID = 1L;

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
