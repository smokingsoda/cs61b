package byow.Core;

public class BYOWException extends RuntimeException {

    /** A GitletException with no message. */
    public BYOWException() {
        super();
    }

    /** A GitletException MSG as its message. */
    public BYOWException(String msg) {
        super(msg);
    }
}
