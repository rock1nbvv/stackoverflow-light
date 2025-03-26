package rockinbvv.stackoverflowlight.app.exception;

public class UserNotFoundException extends ApplicationException {

    public UserNotFoundException(Long userId) {
        super("User not found with id: " + userId);
    }

    @Override
    public String getErrorCode() {
        return "USER_NOT_FOUND";
    }

    @Override
    public int getHttpStatus() {
        return 404;
    }
}
