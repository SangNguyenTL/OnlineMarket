package OnlineMarket.service;

import OnlineMarket.model.Event;
import OnlineMarket.model.Order;

public interface OrderService{
    void save(Order entity);

    void update(Order entity);

    void delete(Order entity);

    Order getByKey(Integer key);

    Order getByDeclaration(String key, Object value);

    Order getByEvent(Event event);
}
