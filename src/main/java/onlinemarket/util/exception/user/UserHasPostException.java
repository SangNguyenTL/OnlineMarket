package onlinemarket.util.exception.user;

public class UserHasPostException extends Exception {
    public UserHasPostException() {
        super("User has post.");
    }

    public UserHasPostException(String message) {
        super(message);
    }
}
