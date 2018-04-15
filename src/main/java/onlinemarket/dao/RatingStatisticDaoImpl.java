package onlinemarket.dao;

import onlinemarket.model.RatingStatistic;

public class RatingStatisticDaoImpl extends AbstractDao<Integer, RatingStatistic> implements RatingStatisticDao {

    @Override
    public void update(RatingStatistic entity) {
        getSession().saveOrUpdate(entity);
    }
}
