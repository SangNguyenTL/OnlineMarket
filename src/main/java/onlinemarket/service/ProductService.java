package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.model.Product;
import onlinemarket.model.ProductCategory;
import onlinemarket.result.ResultObject;

public interface ProductService extends InterfaceService<Integer, Product> {

    Product getByBrand(Brand brand);

    Product getByEvent(Event event);

    Product getByProductCategory(ProductCategory productCategory);

    ResultObject<Product> list(FilterForm filterForm);

    ResultObject<Product> listByProductCategory(ProductCategory productCategory, FilterForm filterForm);

    ResultObject<Product> listByBrand(Brand brand, FilterForm filterForm);

}
