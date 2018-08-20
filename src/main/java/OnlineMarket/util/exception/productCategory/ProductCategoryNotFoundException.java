package OnlineMarket.util.exception.productCategory;

public class ProductCategoryNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public ProductCategoryNotFoundException() {
        super("The productCategory isn't exist.");
    }
}
