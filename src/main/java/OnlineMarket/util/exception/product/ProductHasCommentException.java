package OnlineMarket.util.exception.product;

public class ProductHasCommentException extends Exception{

    private static final long serialVersionUID = 1L;

    public ProductHasCommentException() {
        super("Product has comment.");
    }
}
