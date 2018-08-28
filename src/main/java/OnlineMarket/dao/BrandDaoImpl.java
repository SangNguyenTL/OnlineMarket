package OnlineMarket.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import OnlineMarket.model.Brand;
import OnlineMarket.model.Event;

import java.util.List;

@Repository("brandDao")
public class BrandDaoImpl extends AbstractDao<Integer, Brand> implements BrandDao{

	@Override
	public Brand getByEvent(Event event) {
		Criteria criteria = createEntityCriteria();
		criteria.createAlias("events", "eventsAlias");
		criteria.add(Restrictions.eq("eventsAlias.id", event.getId()));
		return (Brand) criteria.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Brand> list() {
		Criteria criteria = createEntityCriteria();
		criteria.addOrder(Order.asc("name"));
		return (List<Brand>) criteria.list();
	}
}
