package onlinemarket.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import onlinemarket.model.Event;
import onlinemarket.model.ProductCategory;

@Repository("productCategoryDao")
public abstract class ProductCategoryDaoImpl extends AbstractDao<Integer, ProductCategory> implements ProductCategoryDao{

	@Override
	public ProductCategory getByEvent(Event event) {
		Criteria criteria = createEntityCriteria();
		criteria.createAlias("events", "eventsAlias");
		criteria.add(Restrictions.eq("eventsAlias.id", event.getId()));
		return (ProductCategory) criteria.uniqueResult();
	}

	
}
