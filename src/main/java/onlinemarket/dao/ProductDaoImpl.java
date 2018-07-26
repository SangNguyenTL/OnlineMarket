package onlinemarket.dao;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.model.Product;
import onlinemarket.model.ProductCategory;
import onlinemarket.result.ResultObject;

import java.util.List;

@Repository("productDao")
public class ProductDaoImpl extends AbstractDao<Integer, Product> implements ProductDao{
    @Override
    public List<Product> getRelatedProduct(ProductCategory productCategory, Brand brand, Product product) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("productCategory", productCategory));
        criteria.add(Restrictions.eq("brand", brand));
        criteria.add(Restrictions.in("state", new Byte[]{0,1,2} ));
        criteria.add(Restrictions.ne("id", product.getId()));
        criteria.setMaxResults(10);
        return (List<Product>) criteria.list();
    }
}
