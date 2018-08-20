package OnlineMarket.dao;

import OnlineMarket.model.Product;
import OnlineMarket.model.ProductViews;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Calendar;

@Repository("productViewsDao")
public class ProductViewsDaoImpl extends AbstractDao<Integer, ProductViews> implements ProductViewsDao{
    @Override
    public ProductViews getByProductInCurrentDay(Product product) {

        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("product", product));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.add(Calendar.DATE, +1);
        criteria.add(Restrictions.le("datetime", cal.getTime()));
        cal.add(Calendar.DATE, -1);
        criteria.add(Restrictions.ge("datetime", cal.getTime()));

        return (ProductViews) criteria.uniqueResult();
    }

    @Override
    public void update(ProductViews entity) {
        getSession().saveOrUpdate(entity);
    }
}
