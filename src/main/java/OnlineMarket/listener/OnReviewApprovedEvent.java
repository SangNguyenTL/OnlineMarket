package OnlineMarket.listener;

import OnlineMarket.model.Rating;
import org.springframework.context.ApplicationEvent;

public class OnReviewApprovedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private final Rating rating;

    public OnReviewApprovedEvent(Rating rating) {
        super(rating);
        this.rating = rating;
    }

    public Rating getRating() {
        return rating;
    }

}
