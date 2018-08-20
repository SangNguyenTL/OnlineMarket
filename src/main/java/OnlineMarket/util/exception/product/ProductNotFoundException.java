package OnlineMarket.util.exception.product;

public class ProductNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public ProductNotFoundException() {
        super("Product not found.");
    }
}
