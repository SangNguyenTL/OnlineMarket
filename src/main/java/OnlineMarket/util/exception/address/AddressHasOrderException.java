package OnlineMarket.util.exception.address;

public class AddressHasOrderException extends Exception {

    private static final long serialVersionUID = 1L;

    public AddressHasOrderException() {
        super("The address has order.");
    }
}
