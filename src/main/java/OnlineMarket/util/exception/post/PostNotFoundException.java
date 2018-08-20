package OnlineMarket.util.exception.post;

public class PostNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public PostNotFoundException() {
        super("Post not found.");
    }
}
