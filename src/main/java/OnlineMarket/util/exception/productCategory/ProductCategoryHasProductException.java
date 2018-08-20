package OnlineMarket.util.exception.productCategory;

public class ProductCategoryHasProductException extends Exception{

    private static final long serialVersionUID = 1L;

    public ProductCategoryHasProductException() {
        super("The product category had products.");
    }
}
