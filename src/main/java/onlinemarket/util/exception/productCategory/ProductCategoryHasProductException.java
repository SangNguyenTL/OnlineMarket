package onlinemarket.util.exception.productCategory;

public class ProductCategoryHasProductException extends Exception{
    public ProductCategoryHasProductException() {
        super("The product category had products.");
    }
}
