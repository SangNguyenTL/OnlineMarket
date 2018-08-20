package OnlineMarket.util.exception.user;

public class UserHasPostException extends Exception {

    private static final long serialVersionUID = 1L;

    public UserHasPostException() {
        super("User has post.");
    }

    public UserHasPostException(String message) {
        super(message);
    }
}
