package onlinemarket.dao;

import onlinemarket.model.Brand;
import onlinemarket.model.Product;

public interface ProductDao extends InterfaceDao<Integer, Product>{
	
	public Product getByBrand(Brand brand);
	
}
