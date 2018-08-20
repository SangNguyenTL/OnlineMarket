package OnlineMarket.dao;

import OnlineMarket.model.Event;
import OnlineMarket.model.ProductCategory;

public interface ProductCategoryDao extends InterfaceDao<Integer, ProductCategory> {

	ProductCategory getByEvent(Event event);

}
