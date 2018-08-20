package OnlineMarket.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import OnlineMarket.model.Brand;
import OnlineMarket.model.Product;
import OnlineMarket.model.ProductCategory;

import java.util.List;

@Repository("productDao")
public class ProductDaoImpl extends AbstractDao<Integer, Product> implements ProductDao{
    @Override
    @SuppressWarnings("unchecked")
    public List<Product> getRelatedProduct(ProductCategory productCategory, Brand brand, Product product) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("productCategory", productCategory));
        criteria.add(Restrictions.eq("brand", brand));
        criteria.add(Restrictions.in("state", new Byte[]{0,1,2} ));
        criteria.add(Restrictions.ne("id", product.getId()));
        criteria.setMaxResults(10);
        return criteria.list();
    }
}
