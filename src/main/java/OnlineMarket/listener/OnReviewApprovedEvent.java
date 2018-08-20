package OnlineMarket.listener;

import OnlineMarket.model.Rating;
import org.springframework.context.ApplicationEvent;

public class OnReviewApprovedEvent extends ApplicationEvent {


    private final Rating rating;

    public OnReviewApprovedEvent(Rating rating) {
        super(rating);
        this.rating = rating;
    }

    public Rating getRating() {
        return rating;
    }

}
