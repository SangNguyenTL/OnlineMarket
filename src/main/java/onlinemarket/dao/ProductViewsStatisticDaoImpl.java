package onlinemarket.dao;

import onlinemarket.model.ProductViewsStatistic;
import org.springframework.stereotype.Repository;

@Repository("productViewsStatisticDao")
public class ProductViewsStatisticDaoImpl extends AbstractDao<Integer, ProductViewsStatistic> implements ProductViewsStatisticDao{

    @Override
    public void update(ProductViewsStatistic entity) {
        getSession().saveOrUpdate(entity);
    }
}
