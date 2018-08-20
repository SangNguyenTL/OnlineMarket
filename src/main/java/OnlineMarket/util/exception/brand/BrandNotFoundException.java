package OnlineMarket.util.exception.brand;

public class BrandNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public BrandNotFoundException() {
        super("Brand not found!");
    }
}
