package OnlineMarket.dao;

import OnlineMarket.model.RatingStatistic;
import org.springframework.stereotype.Repository;

@Repository("ratingStatisticDao")
public class RatingStatisticDaoImpl extends AbstractDao<Integer, RatingStatistic> implements RatingStatisticDao {

    @Override
    public void update(RatingStatistic entity) {
        getSession().saveOrUpdate(entity);
    }

}
