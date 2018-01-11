package onlinemarket.dao;

import onlinemarket.model.Event;
import onlinemarket.model.ProductCategory;

public interface ProductCategoryDao extends InterfaceDao<Integer, ProductCategory> {

	ProductCategory getByEvent(Event event);

}
