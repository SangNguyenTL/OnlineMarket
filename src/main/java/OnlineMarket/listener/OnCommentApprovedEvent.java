package OnlineMarket.listener;

import OnlineMarket.model.Comment;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletRequest;

public class OnCommentApprovedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    final private Comment comment;

    private HttpServletRequest request;

    public OnCommentApprovedEvent(Comment comment, HttpServletRequest request) {
        super(comment);
        this.comment = comment;
        this.request = request;
    }

    public Comment getComment() {
        return comment;
    }

    public HttpServletRequest getRequest() {
        return request;
    }
}
