package OnlineMarket.util.exception;

public class AddressNotFoundException extends Exception{

    private static final long serialVersionUID = 1L;

    public AddressNotFoundException() {
        super("Address not found");
    }

}
