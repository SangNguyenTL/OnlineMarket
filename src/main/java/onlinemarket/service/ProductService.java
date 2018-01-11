package onlinemarket.service;

import onlinemarket.model.Brand;
import onlinemarket.model.Product;
import onlinemarket.model.ProductCategory;

public interface ProductService extends InterfaceService<Integer, Product>{
	
	public Product getByBrand(Brand brand);
	
	public Product getByProductCategory(ProductCategory productCategory);
}
