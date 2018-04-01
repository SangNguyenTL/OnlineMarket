package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.result.ResultObject;

public interface BrandService extends InterfaceService<Integer, Brand> {

    ResultObject<Brand> pagination(Integer currentPage, Integer size);

    ResultObject<Brand> list(FilterForm filterForm);

    Brand getByEvent(Event event);

}
