package OnlineMarket.util.exception.event;

public class EventNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public EventNotFoundException() {
        super("Event not found.");
    }
}
