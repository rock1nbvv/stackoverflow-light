package rockinbvv.stackoverflowlight.app.exception;

public class InvalidPasswordException extends ApplicationException {

    public InvalidPasswordException() {
        super("Invalid password");
    }

    @Override
    public String getErrorCode() {
        return "INVALID_PASSWORD";
    }

    @Override
    public int getHttpStatus() {
        return 400;
    }
}
