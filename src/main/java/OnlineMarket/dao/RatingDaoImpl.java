package OnlineMarket.dao;


import OnlineMarket.model.Rating;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository("ratingDao")
public class RatingDaoImpl extends AbstractDao<Integer, Rating> implements RatingDao {

    @Override
    public Rating getByUserIdAndProductId(int userId, int productId) {

        Criteria criteria = createEntityCriteria();
        criteria.createAlias("user", "userAlias");
        criteria.createAlias("product", "productAlias");
        criteria.add(Restrictions.eq("userAlias.id", userId));
        criteria.add(Restrictions.eq("productAlias.id", productId));
        return (Rating) criteria.uniqueResult();
    }

}
