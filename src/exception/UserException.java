package exception;

public class UserException extends Exception {
    public UserException() {
        super("No user with this name");
    }
}
