package OnlineMarket.listener;

import OnlineMarket.model.Rating;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletRequest;

public class OnReviewApprovedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private final Rating rating;

    private final HttpServletRequest request;

    public OnReviewApprovedEvent(Rating rating, HttpServletRequest request) {
        super(rating);
        this.rating = rating;
        this.request = request;
    }

    public Rating getRating() {
        return rating;
    }

    public HttpServletRequest getRequest() {
        return request;
    }
}
