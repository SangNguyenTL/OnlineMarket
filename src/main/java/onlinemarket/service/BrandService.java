package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.result.ResultObject;

public interface BrandService extends InterfaceService<Integer, Brand>{
	
	public ResultObject<Brand> pagination(Integer currentPage, Integer size);
	
	public ResultObject<Brand> list(FilterForm filterForm);
	
	public Brand getByEvent(Event event);
	
}
