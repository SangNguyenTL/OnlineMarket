package OnlineMarket.dao;

import OnlineMarket.model.Brand;
import OnlineMarket.model.Product;
import OnlineMarket.model.ProductCategory;

import java.util.List;

public interface ProductDao extends InterfaceDao<Integer, Product>{

    List<Product> getRelatedProduct(ProductCategory productCategory, Brand brand, Product product);
}
