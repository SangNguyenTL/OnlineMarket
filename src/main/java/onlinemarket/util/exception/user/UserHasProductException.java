package onlinemarket.util.exception.user;

public class UserHasProductException extends Exception{
    public UserHasProductException() {
        super("User has product");
    }

    public UserHasProductException(String message) {
        super(message);
    }
}
