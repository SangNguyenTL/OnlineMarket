package OnlineMarket.util.exception.post;

public class PostHasCommentException extends Exception{

    private static final long serialVersionUID = 1L;

    public PostHasCommentException() {
        super("Post has comment.");
    }
}
