package OnlineMarket.dao;

import OnlineMarket.model.Notification;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository("notificationDao")
public class NotificationDaoImpl extends AbstractDao<Integer, Notification> implements NotificationDao{

    @Override
    public long countBy(String key, Object value) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq(key, value));
        criteria.add(Restrictions.eq("status", (byte) 0));
        criteria.setProjection(Projections.rowCount());
        return (long) criteria.uniqueResult();
    }
}
