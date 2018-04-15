package onlinemarket.dao;

import onlinemarket.model.RatingStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ratingStatisticDao")
public class RatingStatisticDaoImpl extends AbstractDao<Integer, RatingStatistic> implements RatingStatisticDao {

    @Override
    public void update(RatingStatistic entity) {
        getSession().saveOrUpdate(entity);
    }

}
