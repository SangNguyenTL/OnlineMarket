package OnlineMarket.listener;

import OnlineMarket.model.Comment;
import OnlineMarket.model.Notification;
import OnlineMarket.service.NotificationService;
import OnlineMarket.util.exception.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CommentApproveListener implements ApplicationListener<OnCommentApprovedEvent> {

    @Autowired
    private NotificationService notificationService;

    private String contextPath;

    @Override
    public void onApplicationEvent(OnCommentApprovedEvent onCommentApprovedEvent) {
        contextPath = onCommentApprovedEvent.getRequest().getContextPath();
        makeNotifyToUser(onCommentApprovedEvent.getComment());
    }

    private void makeNotifyToUser(Comment comment){
        String message = "<a class='text-info' href='"+contextPath+"/post/"+comment.getPost().getSlug()+"'>Your comment on "+comment.getPost().getTitle()+"</a> is approved.";
        try {
            notificationService.add(new Notification(comment.getUser(), message));
        } catch (UserNotFoundException ignore) {
        }
    }
}
