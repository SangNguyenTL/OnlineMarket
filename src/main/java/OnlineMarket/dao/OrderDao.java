package OnlineMarket.dao;

import OnlineMarket.model.Event;
import OnlineMarket.model.Order;

public interface OrderDao extends InterfaceDao<Integer, Order>{

	Order getByEvent(Event event);

}
