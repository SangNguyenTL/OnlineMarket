package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.model.ProductCategory;
import onlinemarket.result.ResultObject;

public interface EventService extends InterfaceService<Integer, Event> {

	public Event getByBrand(Brand brand);

	public Event getByProductCategory(ProductCategory productCategoryCheck);
	
	public ResultObject<Event> list(FilterForm filterForm);
	
}
