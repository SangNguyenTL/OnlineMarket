package onlinemarket.util.exception.user;

public class UserNotFoundException extends Exception{
    public UserNotFoundException() {
        super("User not found.");
    }
}
