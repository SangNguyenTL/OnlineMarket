package onlinemarket.dao;

import onlinemarket.model.Product;
import onlinemarket.model.ProductViews;

import java.util.Date;

public interface ProductViewsDao extends InterfaceDao<Integer, ProductViews> {

    ProductViews getByProductInCurrentDay(Product product);

}
