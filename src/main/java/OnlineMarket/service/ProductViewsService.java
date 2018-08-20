package OnlineMarket.service;

import OnlineMarket.model.Product;
import OnlineMarket.model.ProductViews;

public interface ProductViewsService {

    ProductViews getByProductInCurrentDay(Product product);

    void save(Product product);

}
