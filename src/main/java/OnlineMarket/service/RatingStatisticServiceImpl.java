package OnlineMarket.service;

import OnlineMarket.dao.RatingStatisticDao;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.RatingStatistic;
import OnlineMarket.result.ResultObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("ratingStatisticService")
@Transactional
public class RatingStatisticServiceImpl implements RatingStatisticService{

    @Autowired
    RatingStatisticDao ratingStatisticDao;

    @Override
    public ResultObject<RatingStatistic> list(FilterForm filterForm) {
        return ratingStatisticDao.list(filterForm);
    }
}
