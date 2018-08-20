package OnlineMarket.util.exception.brand;

public class BrandHasProductException extends Exception{

    private static final long serialVersionUID = 1L;

    public BrandHasProductException() {
        super("Brand has product.");
    }
}
