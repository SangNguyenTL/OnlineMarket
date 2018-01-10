package onlinemarket.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import onlinemarket.model.Brand;
import onlinemarket.model.Event;

@Repository("eventDao")
public class EventDaoImpl extends AbstractDao<Integer, Event> implements EventDao {

	@Override
	public Event getByBrand(Brand brand) {
		Criteria criteria = createEntityCriteria();
		criteria.createAlias("brands", "brandsAlias");
		criteria.add(Restrictions.eq("brandsAlias.id", brand.getId()));
		return (Event) criteria.uniqueResult();
	}
	
}
