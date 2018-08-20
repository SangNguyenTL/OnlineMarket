package OnlineMarket.service;

import OnlineMarket.model.Event;
import OnlineMarket.model.Order;

public interface OrderService extends InterfaceService<Integer, Order> {
    Order getByEvent(Event event);
}
