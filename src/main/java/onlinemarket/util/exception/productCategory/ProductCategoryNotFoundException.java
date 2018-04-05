package onlinemarket.util.exception.productCategory;

public class ProductCategoryNotFoundException extends Exception {
    public ProductCategoryNotFoundException() {
        super("The productCategory isn't exist.");
    }
}
