package onlinemarket.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import onlinemarket.model.Attribute;
import onlinemarket.model.ProductAttribute;
import onlinemarket.model.ProductAttributeId;

@Repository("productAttributeDao")
public abstract class ProductAttributeDaoImpl extends AbstractDao<ProductAttributeId, ProductAttribute> implements ProductAttributeDao{

	@Override
	public ProductAttribute getByAttribute(Attribute attribute) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("attribute", attribute));
		return (ProductAttribute) criteria.uniqueResult();
	}

}
