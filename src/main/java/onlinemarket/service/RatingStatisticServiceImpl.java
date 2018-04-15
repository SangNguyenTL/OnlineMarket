package onlinemarket.service;

import onlinemarket.dao.RatingStatisticDao;
import onlinemarket.model.RatingStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("ratingStatisticService")
@Transactional
public class RatingStatisticServiceImpl implements RatingStatisticService{

    @Autowired
    RatingStatisticDao ratingStatisticDao;

    @Override
    public List<RatingStatistic> list() {
        return ratingStatisticDao.list();
    }
}
