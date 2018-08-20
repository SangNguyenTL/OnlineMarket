package OnlineMarket.dao;

import OnlineMarket.model.PasswordResetToken;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository("passwordResetTokenDao")
@Transactional
public class PasswordResetTokenDaoImpl extends AbstractDao<Long, PasswordResetToken> implements PasswordResetTokenDao {

    @Override
    public void deleteAllExpiredSince(Date date) {
        String hql = "delete from PasswordResetToken t where t.expiryDate <= :date";
        getSession().createQuery(hql).setDate("date", date).executeUpdate();
    }

}
