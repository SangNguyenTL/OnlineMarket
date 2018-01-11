package onlinemarket.service;

import onlinemarket.model.Event;
import onlinemarket.model.Order;

public interface OrderService extends InterfaceService<Integer, Order>{
	public Order getByEvent(Event event);
}
