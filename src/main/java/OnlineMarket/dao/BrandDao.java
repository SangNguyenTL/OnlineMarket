package OnlineMarket.dao;

import OnlineMarket.model.Brand;
import OnlineMarket.model.Event;

public interface BrandDao extends InterfaceDao<Integer, Brand>{

	Brand getByEvent(Event event);

}
