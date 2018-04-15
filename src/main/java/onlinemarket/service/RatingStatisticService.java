package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.RatingStatistic;
import onlinemarket.result.ResultObject;

import java.util.List;

public interface RatingStatisticService {

    ResultObject<RatingStatistic> list(FilterForm filterForm);
}
