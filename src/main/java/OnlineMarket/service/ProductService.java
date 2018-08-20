package OnlineMarket.service;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.form.filter.SearchSelect;
import OnlineMarket.model.*;
import OnlineMarket.result.ResultObject;
import OnlineMarket.result.api.ResultProduct;
import OnlineMarket.util.exception.product.ProductHasCommentException;
import OnlineMarket.util.exception.product.ProductNotFoundException;
import OnlineMarket.util.exception.productCategory.ProductCategoryNotFoundException;
import OnlineMarket.view.FrontendProduct;

import java.util.List;

public interface ProductService {

    Product getByKey(Integer key);

    Product getByDeclaration(String key, Object value);

    void save(Product product, ProductCategory productCategory, User user) throws ProductCategoryNotFoundException;

    void update(Product product, ProductCategory productCategory, User user) throws ProductCategoryNotFoundException, ProductNotFoundException;

    void delete(Integer id) throws ProductNotFoundException;

    ResultObject<Product> listByProductCategory(ProductCategory productCategory, FilterForm filterForm) throws ProductCategoryNotFoundException;

    ResultObject<FrontendProduct> frontendProductResultObject(ResultObject<Product> productResultObject);

    ResultObject<Product> list(FilterForm filterForm);

    ResultObject<Product> listByInList(String key, List<Object> listValue, FilterForm filterForm);

    ResultProduct searchSelect(SearchSelect searchSelect);

    List<Product> getRelatedProduct(ProductCategory productCategory, Brand brand, Product product);

    List<FrontendProduct> convertProductToFrProduct(List<Product> productList);

    FrontendProduct convertProductToFrProduct(Product product);
}
