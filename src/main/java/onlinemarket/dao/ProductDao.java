package onlinemarket.dao;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.model.Product;
import onlinemarket.model.ProductCategory;
import onlinemarket.result.ResultObject;

public interface ProductDao extends InterfaceDao<Integer, Product>{
	
	public Product getByBrand(Brand brand);
	
	public Product getByProductCategory(ProductCategory productCategory);

	public Product getByEvent(Event event);

	public ResultObject<Product> listByProductCategory(ProductCategory productCategory, FilterForm filterForm);

	public ResultObject<Product> listByProductBrand(Brand brand, FilterForm filterForm);

}
