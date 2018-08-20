package OnlineMarket.listener;

import OnlineMarket.model.User;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletRequest;


@SuppressWarnings("serial")
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private final HttpServletRequest request;
    private final User user;

    public OnRegistrationCompleteEvent(final User user, HttpServletRequest request) {
        super(user);
        this.user = user;
        this.request = request;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public User getUser() {
        return user;
    }

}
