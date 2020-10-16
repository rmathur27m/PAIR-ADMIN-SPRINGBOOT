package gov.uspto.patent.privatePair.exceptionhandlers;

/**
 * 
 * POJO used to wrap custom exception extends java.lang.Exception.
 * 
 */
public class UserNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
