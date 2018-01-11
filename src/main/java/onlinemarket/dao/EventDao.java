package onlinemarket.dao;

import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.model.ProductCategory;
import onlinemarket.model.User;

public interface EventDao extends InterfaceDao<Integer, Event>{

	public Event getByBrand(Brand brand);

	public Event getByProductCategory(ProductCategory productCategoryCheck);

	public Event getByUser(User user);
	
}
