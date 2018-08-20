package OnlineMarket.service;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Province;
import OnlineMarket.result.ResultObject;

public interface ProvinceService extends InterfaceService<Integer, Province> {
    ResultObject<Province> list(FilterForm filterForm);
}
