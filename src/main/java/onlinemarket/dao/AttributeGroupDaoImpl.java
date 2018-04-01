package onlinemarket.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.AttributeGroup;
import onlinemarket.model.ProductCategory;
import onlinemarket.result.ResultObject;

@Repository("attributeGroupDao")
public class AttributeGroupDaoImpl extends AbstractDao<Integer, AttributeGroup> implements AttributeGroupDao {

	@Override
	public AttributeGroup getByProductCategory(ProductCategory productCategory) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("productCategory", productCategory));
		return (AttributeGroup) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultObject<AttributeGroup> listByProductCategory(ProductCategory productCategory, FilterForm filterForm) {

		Criteria criteria = createEntityCriteria();
		ResultObject<AttributeGroup> result = new ResultObject<>();

		criteria.add(Restrictions.eq("productCategory", productCategory));

		return childFilterFrom(criteria, filterForm);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AttributeGroup> listByProductCategory(ProductCategory productCategory) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("productCategory", productCategory));
		return criteria.list();
	}

}
