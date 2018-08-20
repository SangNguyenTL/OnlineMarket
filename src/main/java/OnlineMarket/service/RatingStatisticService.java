package OnlineMarket.service;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.RatingStatistic;
import OnlineMarket.result.ResultObject;

public interface RatingStatisticService {

    ResultObject<RatingStatistic> list(FilterForm filterForm);
}
