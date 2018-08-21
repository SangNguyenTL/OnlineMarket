package OnlineMarket.listener;

import OnlineMarket.model.Notification;
import OnlineMarket.model.Rating;
import OnlineMarket.service.NotificationService;
import OnlineMarket.util.exception.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ReviewApprovedListener implements ApplicationListener<OnReviewApprovedEvent> {

    @Autowired
    private NotificationService notificationService;

    private String contextPath;

    @Override
    public void onApplicationEvent(OnReviewApprovedEvent onReviewApprovedEvent) {
        contextPath = onReviewApprovedEvent.getRequest().getContextPath();
        makeNotifyToUser(onReviewApprovedEvent.getRating());
    }

    private void makeNotifyToUser(Rating rating){
        String message = "<a class='text-info' href='"+contextPath+"/product/"+rating.getProduct().getSlug()+"'>Your review on "+rating.getProduct().getName()+"</a> is approved.";
        try {
            notificationService.add(new Notification(rating.getUser(), message));
        } catch (UserNotFoundException ignore) {
        }
    }
}
