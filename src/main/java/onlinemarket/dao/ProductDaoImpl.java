package onlinemarket.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import onlinemarket.model.Brand;
import onlinemarket.model.Product;
import onlinemarket.model.ProductCategory;

@Repository("productDao")
public class ProductDaoImpl extends AbstractDao<Integer, Product> implements ProductDao{

	@Override
	public Product getByBrand(Brand brand) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("brand", brand));
		return (Product) criteria.uniqueResult();
	}

	@Override
	public Product getByProductCategory(ProductCategory productCategory) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("productCategory", productCategory));
		return (Product) criteria.uniqueResult();
	}

}
