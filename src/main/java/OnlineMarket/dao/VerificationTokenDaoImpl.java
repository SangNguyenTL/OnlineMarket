package OnlineMarket.dao;

import OnlineMarket.model.User;
import OnlineMarket.model.VerificationToken;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository("verificationTokenDao")
@Transactional
public class VerificationTokenDaoImpl extends AbstractDao<Long, VerificationToken> implements VerificationTokenDao{

    @Override
    public VerificationToken findByToken(String token) {
        return getByDeclaration("token", token);
    }

    @Override
    public VerificationToken findByUser(User user) {
        return getByDeclaration("user", user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<VerificationToken> findAllByExpiryDateLessThan(Date now) {

        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.le("expiryDate", now));

        return criteria.list();
    }

    @Override
    public void deleteByExpiryDateLessThan(Date now) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.le("expiryDate", now));
        delete((VerificationToken) criteria.uniqueResult());
    }

    @Override
    public void deleteAllExpiredSince(Date now) {
        String hql = "delete from VerificationToken t where t.expiryDate <= :date";
        getSession().createQuery(hql).setDate("date", now).executeUpdate();
    }
}
