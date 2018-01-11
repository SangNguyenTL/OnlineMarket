package onlinemarket.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import onlinemarket.model.Attribute;
import onlinemarket.model.AttributeGroup;

@Repository("attributeDao")
public class AttributeDaoImpl extends AbstractDao<Integer, Attribute> implements AttributeDao{

	@Override
	public Attribute getByAttributeGroup(AttributeGroup attributeGroup) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("attributeGroup", attributeGroup));
		return (Attribute) criteria.uniqueResult();
	}

}
