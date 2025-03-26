package rockinbvv.stackoverflowlight.app.exception;

public abstract class ApplicationException extends RuntimeException {

    protected ApplicationException(String message) {
        super(message);
    }

    public abstract String getErrorCode();   // e.g., "INVALID_PASSWORD"
    public abstract int getHttpStatus();     // e.g., 400
}

