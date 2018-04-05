package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Event;
import onlinemarket.model.ProductCategory;
import onlinemarket.result.ResultObject;
import onlinemarket.util.exception.productCategory.ProductCategoryHasAttributeGroupException;
import onlinemarket.util.exception.productCategory.ProductCategoryHasEventException;
import onlinemarket.util.exception.productCategory.ProductCategoryHasProductException;
import onlinemarket.util.exception.productCategory.ProductCategoryNotFoundException;

public interface ProductCategoryService extends InterfaceService<Integer, ProductCategory>{

	ResultObject<ProductCategory> list(FilterForm filterForm);
	
	ProductCategory getByEvent(Event event);

	void delete(Integer id) throws ProductCategoryNotFoundException, ProductCategoryHasProductException, ProductCategoryHasEventException, ProductCategoryHasAttributeGroupException;
}
