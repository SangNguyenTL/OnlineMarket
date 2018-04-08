package onlinemarket.util.exception.product;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException() {
        super("Product not found.");
    }
}
