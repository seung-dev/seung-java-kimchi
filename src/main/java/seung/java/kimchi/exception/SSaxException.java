package seung.java.kimchi.exception;

public class SSaxException extends Exception {

    private static final long serialVersionUID = 8091936058655861833L;
    
    public SSaxException(String message) {
        super(message);
    }
    
    public SSaxException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
