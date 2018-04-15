package onlinemarket.dao;

import onlinemarket.model.Product;
import onlinemarket.model.ProductViews;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;

@Repository("productViewsDao")
public class ProductViewsDaoImpl extends AbstractDao<Integer, ProductViews> implements ProductViewsDao{
    @Override
    public ProductViews getByProductInCurrentDay(Product product) {

        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("product", product));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, +1);
        criteria.add(Restrictions.le("datetime", c.getTime()));
        c.add(Calendar.DATE, -1);
        criteria.add(Restrictions.ge("datetime", c.getTime()));

        return (ProductViews) criteria.uniqueResult();
    }

    @Override
    public void update(ProductViews entity) {
        getSession().saveOrUpdate(entity);
    }
}
