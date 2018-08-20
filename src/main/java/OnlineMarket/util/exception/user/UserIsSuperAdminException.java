package OnlineMarket.util.exception.user;

public class UserIsSuperAdminException extends Exception {

    private static final long serialVersionUID = 1L;

    public UserIsSuperAdminException() {
        super("User is super admin");
    }
}
