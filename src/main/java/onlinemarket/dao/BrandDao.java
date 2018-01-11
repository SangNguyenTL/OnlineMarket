package onlinemarket.dao;

import onlinemarket.model.Brand;
import onlinemarket.model.Event;

public interface BrandDao extends InterfaceDao<Integer, Brand>{

	Brand getByEvent(Event event);

}
