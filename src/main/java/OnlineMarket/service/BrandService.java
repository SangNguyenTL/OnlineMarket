package OnlineMarket.service;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Brand;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.exception.brand.BrandHasEventException;
import OnlineMarket.util.exception.brand.BrandHasProductException;
import OnlineMarket.util.exception.brand.BrandNotFoundException;

import java.util.List;

public interface BrandService {

    Brand getByKey(Integer key);

    Brand getByDeclaration(String key, Object value);

    void save(Brand entity);

    void delete(Integer id) throws BrandNotFoundException, BrandHasEventException, BrandHasProductException;

    void update(Brand entity) throws BrandNotFoundException;

    ResultObject<Brand> list(FilterForm filterForm);

    List<Brand> list();

}
