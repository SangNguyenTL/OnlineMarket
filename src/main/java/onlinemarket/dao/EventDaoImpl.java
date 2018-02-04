package onlinemarket.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.model.ProductCategory;
import onlinemarket.model.User;

@Repository("eventDao")
public abstract class EventDaoImpl extends AbstractDao<Integer, Event> implements EventDao {

	@Override
	public Event getByBrand(Brand brand) {
		Criteria criteria = createEntityCriteria();
		criteria.createAlias("brands", "brandsAlias");
		criteria.add(Restrictions.eq("brandsAlias.id", brand.getId()));
		return (Event) criteria.uniqueResult();
	}

	@Override
	public Event getByProductCategory(ProductCategory productCategoryCheck) {
		Criteria criteria = createEntityCriteria();
		criteria.createAlias("productCategories", "productCategoriesAlias");
		criteria.add(Restrictions.eq("productCategoriesAlias.id", productCategoryCheck.getId()));
		return (Event) criteria.uniqueResult();
	}

	@Override
	public Event getByUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
