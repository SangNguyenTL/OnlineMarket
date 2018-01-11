package onlinemarket.dao;

import onlinemarket.model.Event;
import onlinemarket.model.Order;

public interface OrderDao extends InterfaceDao<Integer, Order>{

	Order getByEvent(Event event);

}
