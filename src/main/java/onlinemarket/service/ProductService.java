package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.model.Product;
import onlinemarket.model.ProductCategory;
import onlinemarket.result.ResultObject;

public interface ProductService extends InterfaceService<Integer, Product>{
	
	public Product getByBrand(Brand brand);
	
	public Product getByEvent(Event event);
	
	public Product getByProductCategory(ProductCategory productCategory);

	public ResultObject<Product> list(FilterForm filterForm);

	public ResultObject<Product> listByProductCategory(ProductCategory productCategory, FilterForm filterForm);

	ResultObject<Product> listByBrand(Brand brand, FilterForm filterForm);

}
