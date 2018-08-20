package OnlineMarket.util.exception.postCategory;

public class PostCategoryNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public PostCategoryNotFoundException() {
        super("Post category not found.");
    }
}
