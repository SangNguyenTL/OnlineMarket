package onlinemarket.util.exception.user;

public class UserHasEventException extends Exception{
    public UserHasEventException() {
        super("User has event.");
    }

    public UserHasEventException(String message) {
        super(message);
    }
}
