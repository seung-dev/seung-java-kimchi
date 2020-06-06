package seung.java.kimchi.exception;

/**
 * <pre>
 * exists duplicate data exception
 * </pre>
 * @author seung
 * @since 2020.05.11
 */
public class SIllegalArgumentException extends Exception {

    private static final long serialVersionUID = 1552291746334178040L;

    public SIllegalArgumentException(String message) {
        super(message);
    }
    
    public SIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
