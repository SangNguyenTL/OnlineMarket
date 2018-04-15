package onlinemarket.util.exception.user;

public class UserIsSuperAdminException extends Exception {
    public UserIsSuperAdminException() {
        super("User is super admin");
    }
}
