package onlinemarket.service;

import onlinemarket.model.Brand;
import onlinemarket.model.Event;

public interface EventService extends InterfaceService<Integer, Event> {

	public Event getByBrand(Brand brand);
	
}
