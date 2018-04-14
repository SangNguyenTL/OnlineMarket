package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.form.filter.SearchSelect;
import onlinemarket.model.*;
import onlinemarket.result.ResultObject;
import onlinemarket.result.api.ResultProduct;
import onlinemarket.util.exception.product.ProductHasCommentException;
import onlinemarket.util.exception.product.ProductNotFoundException;
import onlinemarket.util.exception.productCategory.ProductCategoryNotFoundException;

import java.util.List;

public interface ProductService {

    Product getByKey(Integer key);

    Product getByDeclaration(String key, Object value);

    void save(Product product, ProductCategory productCategory, User user) throws ProductCategoryNotFoundException;

    void update(Product product, ProductCategory productCategory, User user) throws ProductCategoryNotFoundException, ProductNotFoundException;

    void delete(Integer id) throws ProductNotFoundException, ProductHasCommentException;

    ResultObject<Product> listByProductCategory(ProductCategory productCategory, FilterForm filterForm) throws ProductCategoryNotFoundException;

    ResultObject<Product> list(FilterForm filterForm);

    ResultProduct searchSelect(SearchSelect searchSelect);

    List<Product> getRelatedProduct(ProductCategory productCategory, Brand brand);
}
