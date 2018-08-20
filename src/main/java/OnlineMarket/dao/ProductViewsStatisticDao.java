package OnlineMarket.dao;

import OnlineMarket.model.ProductViewsStatistic;

public interface ProductViewsStatisticDao extends InterfaceDao<Integer, ProductViewsStatistic>{

    void resetWeek();

    void resetMonth();

    void resetYear();
}
