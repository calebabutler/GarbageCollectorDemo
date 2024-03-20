
/*
 * A simple exception to throw when the garbage collector is "Out of memory."
 */

public class OOMException extends RuntimeException {
    
    public OOMException() {
        super();
    }

    public OOMException(String message) {
        super(message);
    }

}
