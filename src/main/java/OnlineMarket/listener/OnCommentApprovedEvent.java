package OnlineMarket.listener;

import OnlineMarket.model.Comment;
import org.springframework.context.ApplicationEvent;

public class OnCommentApprovedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    final private Comment comment;

    public OnCommentApprovedEvent(Comment comment) {
        super(comment);
        this.comment = comment;
    }

    public Comment getComment() {
        return comment;
    }
}
