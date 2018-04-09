package onlinemarket.util.exception.post;

public class PostNotFoundException extends Exception {
    public PostNotFoundException() {
        super("Post not found.");
    }
}
