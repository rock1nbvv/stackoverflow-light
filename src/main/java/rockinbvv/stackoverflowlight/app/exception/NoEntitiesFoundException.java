package rockinbvv.stackoverflowlight.app.exception;

public class NoEntitiesFoundException extends ApplicationException {

    public NoEntitiesFoundException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "NO_ENTITIES_FOUND";
    }

    @Override
    public int getHttpStatus() {
        return 404;
    }
}
