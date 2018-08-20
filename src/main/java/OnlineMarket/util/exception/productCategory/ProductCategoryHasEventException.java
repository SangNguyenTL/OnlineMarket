package OnlineMarket.util.exception.productCategory;

public class ProductCategoryHasEventException extends Exception{

    private static final long serialVersionUID = 1L;

    public ProductCategoryHasEventException() {
        super("The product category had events.");
    }
}
