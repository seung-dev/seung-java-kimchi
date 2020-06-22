package seung.java.kimchi.util;

public class SKimchiException extends Exception {

    private static final long serialVersionUID = 5500146280392382953L;
    
    public SKimchiException(Throwable e) {
        super(e);
    }
    
    public SKimchiException(String message) {
        super(message);
    }
    
    public SKimchiException(String message, Throwable e) {
        super(message, e);
    }
    
}
