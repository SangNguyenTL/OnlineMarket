package onlinemarket.service;

import onlinemarket.model.Brand;
import onlinemarket.model.Product;

public interface ProductService extends InterfaceService<Integer, Product>{
	
	public Product getByBrand(Brand brand);
	
}
