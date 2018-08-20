package OnlineMarket.util.exception.postCategory;

public class PostCategoryHasPostException extends Exception{

    private static final long serialVersionUID = 1L;

    public PostCategoryHasPostException() {
        super("Post category has post.");
    }
}
