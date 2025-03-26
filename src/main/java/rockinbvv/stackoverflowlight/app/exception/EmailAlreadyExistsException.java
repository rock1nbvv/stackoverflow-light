package rockinbvv.stackoverflowlight.app.exception;

public class EmailAlreadyExistsException extends ApplicationException {

    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email);
    }

    @Override
    public String getErrorCode() {
        return "EMAIL_ALREADY_EXISTS";
    }

    @Override
    public int getHttpStatus() {
        return 409;
    }
}
