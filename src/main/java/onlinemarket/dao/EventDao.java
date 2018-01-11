package onlinemarket.dao;

import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.model.ProductCategory;

public interface EventDao extends InterfaceDao<Integer, Event>{

	public Event getByBrand(Brand brand);

	public Event getByProductCategory(ProductCategory productCategoryCheck);
	
}
