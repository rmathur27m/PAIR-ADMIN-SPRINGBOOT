package gov.uspto.patent.privatePair.exceptionhandlers;

/**
 * POJO used to wrap database exceptions extends java.lang.Exception.
 * 
 */

public class PairAdminDatabaseException extends Exception {

    private static final long serialVersionUID = 1L;

    public PairAdminDatabaseException() {
    }

    public PairAdminDatabaseException(String message) {
        super(message);
    }

    public PairAdminDatabaseException(Throwable cause) {
        super(cause);
    }

    public PairAdminDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
