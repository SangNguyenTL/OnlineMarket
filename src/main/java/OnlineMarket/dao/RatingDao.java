package OnlineMarket.dao;

import OnlineMarket.model.Rating;

public interface RatingDao extends InterfaceDao<Integer, Rating>{

    Rating getByUserIdAndProductId(int userId, int productId);

}
