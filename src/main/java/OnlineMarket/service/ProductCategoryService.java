package OnlineMarket.service;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Event;
import OnlineMarket.model.ProductCategory;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.exception.productCategory.ProductCategoryHasAttributeGroupException;
import OnlineMarket.util.exception.productCategory.ProductCategoryHasEventException;
import OnlineMarket.util.exception.productCategory.ProductCategoryHasProductException;
import OnlineMarket.util.exception.productCategory.ProductCategoryNotFoundException;

public interface ProductCategoryService extends InterfaceService<Integer, ProductCategory>{

	ResultObject<ProductCategory> list(FilterForm filterForm);
	
	ProductCategory getByEvent(Event event);

	void delete(Integer id) throws ProductCategoryNotFoundException, ProductCategoryHasProductException, ProductCategoryHasEventException, ProductCategoryHasAttributeGroupException;

    long count();
}
