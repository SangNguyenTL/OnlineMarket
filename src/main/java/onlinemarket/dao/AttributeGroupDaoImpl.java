package onlinemarket.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.AttributeGroup;
import onlinemarket.model.ProductCategory;
import onlinemarket.result.ResultObject;

@Repository("attributeGroupDao")
public class AttributeGroupDaoImpl extends AbstractDao<Integer, AttributeGroup> implements AttributeGroupDao{

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
		
		if(StringUtils.isNotBlank(filterForm.getSearch()) && StringUtils.isNotBlank(filterForm.getSearchBy())) {
			criteria.add(Restrictions.like(filterForm.getSearchBy(), filterForm.getSearch()));
		}
		
		criteria.setProjection(Projections.rowCount());
		Long count = (Long) criteria.uniqueResult();
		if(count != null ) {
			int totalPages = (int) Math.ceil((float) count/filterForm.getSize());
			result.setTotalPages(totalPages);
		}else result.setTotalPages(0);
		
		if(filterForm.getSize() > 0 && filterForm.getCurrentPage() > 0) 
			criteria.setFirstResult((filterForm.getCurrentPage()- 1) * filterForm.getSize()).setMaxResults(filterForm.getSize());
		criteria.setProjection(null);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(StringUtils.isNotBlank(filterForm.getOrderBy()) && StringUtils.isNotBlank(filterForm.getOrder())) {
			if(filterForm.getOrder().equals("asc"))
				criteria.addOrder(Order.asc(filterForm.getOrderBy()));
			else
				criteria.addOrder(Order.desc(filterForm.getOrderBy()));
		}
		
		result.setList(criteria.list());
		
		result.setCurrentPage(filterForm.getCurrentPage());
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AttributeGroup> listWithAttributeByProductCategory(ProductCategory productCategory) {
		
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("productCategory", productCategory));
		criteria.setFetchMode("attributes", FetchMode.JOIN);
		return criteria.list();
		
	}

}
