package OnlineMarket.util.exception;

public class PasswordResetTokenExistingException extends Exception {

    private static final long serialVersionUID = 1L;

    public PasswordResetTokenExistingException(String message) {
        super(message);
    }
}
