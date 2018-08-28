package OnlineMarket.listener;

import OnlineMarket.model.Order;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("serial")
public class OnOrderEvent extends ApplicationEvent {

    private final Order oder;

    private final String action;

    private final HttpServletRequest request;

    public OnOrderEvent(final Order oder, final String action, final HttpServletRequest request) {
        super(oder);
        this.oder = oder;
        this.action = action;
        this.request = request;
    }

    public Order getOder() {
        return oder;
    }

    public String getAction() {
        return action;
    }

    public HttpServletRequest getRequest() {
        return request;
    }
}
