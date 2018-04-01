package onlinemarket.util.exception.productCategory;

public class ProductCategoryNotFound extends Exception {
    public ProductCategoryNotFound() {
        super("The productCategory isn't exist.");
    }
}
