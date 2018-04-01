package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Event;
import onlinemarket.model.ProductCategory;
import onlinemarket.result.ResultObject;
import onlinemarket.util.exception.productCategory.ProductCategoryHasAttributeGroup;
import onlinemarket.util.exception.productCategory.ProductCategoryHasEvent;
import onlinemarket.util.exception.productCategory.ProductCategoryHasProduct;
import onlinemarket.util.exception.productCategory.ProductCategoryNotFound;

public interface ProductCategoryService extends InterfaceService<Integer, ProductCategory>{

	ResultObject<ProductCategory> list(FilterForm filterForm);
	
	ProductCategory getByEvent(Event event);

	void delete(Integer id) throws ProductCategoryNotFound, ProductCategoryHasProduct, ProductCategoryHasEvent, ProductCategoryHasAttributeGroup;
}
