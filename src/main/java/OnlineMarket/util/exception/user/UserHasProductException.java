package OnlineMarket.util.exception.user;

public class UserHasProductException extends Exception{

    private static final long serialVersionUID = 1L;

    public UserHasProductException() {
        super("User has product");
    }

    public UserHasProductException(String message) {
        super(message);
    }
}
