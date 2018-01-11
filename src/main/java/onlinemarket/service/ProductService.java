package onlinemarket.service;

import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.model.Product;
import onlinemarket.model.ProductCategory;

public interface ProductService extends InterfaceService<Integer, Product>{
	
	public Product getByBrand(Brand brand);
	
	public Product getByEvent(Event event);
	
	public Product getByProductCategory(ProductCategory productCategory);
}
