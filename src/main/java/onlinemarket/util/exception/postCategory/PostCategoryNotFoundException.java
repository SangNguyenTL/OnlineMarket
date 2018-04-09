package onlinemarket.util.exception.postCategory;

public class PostCategoryNotFoundException extends Exception {
    public PostCategoryNotFoundException() {
        super("Post category not found.");
    }
}
