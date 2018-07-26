package onlinemarket.dao;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.model.Product;
import onlinemarket.model.ProductCategory;
import onlinemarket.result.ResultObject;

import java.util.List;

public interface ProductDao extends InterfaceDao<Integer, Product>{

    List<Product> getRelatedProduct(ProductCategory productCategory, Brand brand, Product product);
}
