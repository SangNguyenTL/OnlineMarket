package onlinemarket.dao;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Rating;
import onlinemarket.model.User;
import onlinemarket.result.ResultObject;
import org.hibernate.Criteria;

import java.io.Serializable;
import java.util.List;

public class RatingDaoImpl extends AbstractDao<Integer, Rating> implements RatingDao {

    @Override
    ResultObject<Rating> childFilterFrom(Criteria criteria, FilterForm filterForm) {
        return super.childFilterFrom(criteria, filterForm);
    }
}
