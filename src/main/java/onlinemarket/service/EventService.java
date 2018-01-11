package onlinemarket.service;

import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.model.ProductCategory;

public interface EventService extends InterfaceService<Integer, Event> {

	public Event getByBrand(Brand brand);

	public Event getByProductCategory(ProductCategory productCategoryCheck);
	
}
