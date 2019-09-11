package OnlineMarket.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import OnlineMarket.model.ProductViewsStatistic;

@Repository("productViewsStatisticDao")
@Transactional
public class ProductViewsStatisticDaoImpl extends AbstractDao<Integer, ProductViewsStatistic> implements ProductViewsStatisticDao{

    @Override
    public void update(ProductViewsStatistic entity) {
        getSession().saveOrUpdate(entity);
    }

    @Override
    public void resetWeek(){
        getSession().createQuery("update ProductViewsStatistic as p SET week = 0").executeUpdate();
    }

    @Override
    public void resetMonth(){
        getSession().createQuery("update ProductViewsStatistic as p SET month = 0").executeUpdate();
    }

    @Override
    public void resetYear(){
        getSession().createQuery("update ProductViewsStatistic as SET p year = 0").executeUpdate();
    }

}
