package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.result.ResultObject;
import onlinemarket.util.exception.CustomException;
import onlinemarket.util.exception.brand.BrandHasEventException;
import onlinemarket.util.exception.brand.BrandHasProductException;
import onlinemarket.util.exception.brand.BrandNotFoundException;

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
