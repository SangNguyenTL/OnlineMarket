package onlinemarket.service;

import onlinemarket.model.Product;
import onlinemarket.model.ProductViews;

public interface ProductViewsService {

    ProductViews getByProductInCurrentDay(Product product);

    void save(Product product);

}
