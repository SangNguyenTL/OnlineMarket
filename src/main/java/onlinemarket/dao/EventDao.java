package onlinemarket.dao;

import onlinemarket.model.Brand;
import onlinemarket.model.Event;

public interface EventDao extends InterfaceDao<Integer, Event>{

	public Event getByBrand(Brand brand);
	
}
