package OnlineMarket.service;

import java.util.List;
import java.util.Set;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.form.filter.SearchSelect;
import OnlineMarket.model.Brand;
import OnlineMarket.model.Event;
import OnlineMarket.model.Product;
import OnlineMarket.model.ProductCategory;
import OnlineMarket.model.User;
import OnlineMarket.result.ResultObject;
import OnlineMarket.result.api.ResultProduct;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.exception.product.ProductNotFoundException;
import OnlineMarket.util.exception.productCategory.ProductCategoryNotFoundException;
import OnlineMarket.view.FrontendProduct;

public interface ProductService {

    Product getByKey(Integer key);

    Product getByDeclaration(String key, Object value);

    FrontendProduct getFrontendProductByDeclaration(String key, Object value);

    Product save(Product product, ProductCategory productCategory, User user) throws ProductCategoryNotFoundException;

    void update(Product product, ProductCategory productCategory, User user) throws ProductCategoryNotFoundException, ProductNotFoundException;

    void delete(Integer id) throws ProductNotFoundException, CustomException;

    ResultObject<Product> listByProductCategory(ProductCategory productCategory, FilterForm filterForm) throws ProductCategoryNotFoundException;

    ResultObject<FrontendProduct> frontendProductResultObject(ResultObject<Product> productResultObject);

    ResultObject<Product> list(FilterForm filterForm);

    ResultObject<Product> listByInList(String key, List<Object> listValue, FilterForm filterForm);

    ResultProduct searchSelect(SearchSelect searchSelect);

    List<Product> getRelatedProduct(ProductCategory productCategory, Brand brand, Product product);

    List<FrontendProduct> convertProductToFrProduct(List<Product> productList);

    FrontendProduct convertProductToFrProduct(Product product);

    void processEventProduct(FrontendProduct frontendProduct, Set<Event> events);
}
