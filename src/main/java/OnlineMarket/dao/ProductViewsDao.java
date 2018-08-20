package OnlineMarket.dao;

import OnlineMarket.model.Product;
import OnlineMarket.model.ProductViews;

public interface ProductViewsDao extends InterfaceDao<Integer, ProductViews> {

    ProductViews getByProductInCurrentDay(Product product);

}
