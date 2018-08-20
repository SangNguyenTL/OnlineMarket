package OnlineMarket.util.exception.user;

public class UserHasEventException extends Exception{

    private static final long serialVersionUID = 1L;

    public UserHasEventException() {
        super("User has event.");
    }

    public UserHasEventException(String message) {
        super(message);
    }
}
