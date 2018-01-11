package onlinemarket.dao;

import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.model.Product;
import onlinemarket.model.ProductCategory;

public interface ProductDao extends InterfaceDao<Integer, Product>{
	
	public Product getByBrand(Brand brand);
	
	public Product getByProductCategory(ProductCategory productCategory);

	public Product getByEvent(Event event);

}
