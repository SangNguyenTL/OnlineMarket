package onlinemarket.util.exception.address;

public class AddressHasOrderException extends Exception {
    public AddressHasOrderException() {
        super("The address has order.");
    }
}
