package OnlineMarket.util.exception.productCategory;

public class ProductCategoryHasAttributeGroupException extends Exception{

    private static final long serialVersionUID = 1L;

    public ProductCategoryHasAttributeGroupException() {
        super("The product category has attribute group.");
    }
}
