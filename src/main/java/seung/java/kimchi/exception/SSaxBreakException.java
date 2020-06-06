package seung.java.kimchi.exception;

public class SSaxBreakException extends Exception {

	private static final long serialVersionUID = -5424904345140589249L;
	
	public SSaxBreakException(String message) {
		super(message);
	}
	
	public SSaxBreakException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
