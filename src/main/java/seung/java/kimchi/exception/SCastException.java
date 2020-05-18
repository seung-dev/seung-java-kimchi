package seung.java.kimchi.exception;

/**
 * <pre>
 * convert data type exception
 * </pre>
 * @author seung
 * @since 2020.05.11
 */
public class SCastException extends Exception {

	private static final long serialVersionUID = -8753365884400694041L;
	
	public SCastException(String message) {
		super(message);
	}
	
	public SCastException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
