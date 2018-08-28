package OnlineMarket.dao;

import OnlineMarket.model.Event;
import OnlineMarket.model.Order;
import OnlineMarket.model.User;

public interface OrderDao extends InterfaceDao<Integer, Order>{

	Order getByEvent(Event event);

    Order getByCodeAndUser(String code, User user);
}
