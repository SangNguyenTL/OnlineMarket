package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Province;
import onlinemarket.result.ResultObject;

public interface ProvinceService extends InterfaceService<Integer, Province> {
    ResultObject<Province> list(FilterForm filterForm);
}
