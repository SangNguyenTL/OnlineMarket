package OnlineMarket.listener;

import OnlineMarket.model.Comment;
import org.springframework.context.ApplicationEvent;

public class OnCommentApprovedEvent extends ApplicationEvent {

    final private Comment comment;

    public OnCommentApprovedEvent(Comment comment) {
        super(comment);
        this.comment = comment;
    }

    public Comment getComment() {
        return comment;
    }
}
