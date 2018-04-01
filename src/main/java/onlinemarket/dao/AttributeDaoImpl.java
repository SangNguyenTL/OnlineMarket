package onlinemarket.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Attribute;
import onlinemarket.model.AttributeGroup;
import onlinemarket.result.ResultObject;

@Repository("attributeDao")
public class AttributeDaoImpl extends AbstractDao<Integer, Attribute> implements AttributeDao{

	@Override
	public Attribute getByAttributeGroup(AttributeGroup attributeGroup) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("attributeGroup", attributeGroup));
		return (Attribute) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultObject<Attribute> listByAttributeGroup(AttributeGroup attributeGroup, FilterForm filterForm) {
		
		Criteria criteria = createEntityCriteria();
		ResultObject<Attribute> result = new ResultObject<>();
		
		criteria.add(Restrictions.eq("attributeGroup", attributeGroup));
		
		return childFilterFrom(criteria, filterForm);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Attribute> listByAttributeGroupNoneFilter(AttributeGroup attributeGroup) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("attributeGroup", attributeGroup));
		return criteria.list();
	}

}
