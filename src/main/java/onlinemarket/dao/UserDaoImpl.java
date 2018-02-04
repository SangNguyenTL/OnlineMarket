package onlinemarket.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import onlinemarket.model.Event;
import onlinemarket.model.User;

@Repository("userDao")
public abstract class UserDaoImpl extends AbstractDao<Integer, User> implements UserDao {

	@Override
	public User getByEmail(String email) {
		try {
			Criteria criteria = createEntityCriteria();
			criteria.add(Restrictions.eq("email", email));
			return (User) criteria.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public User getByEvent(Event event) {
		Criteria criteria = createEntityCriteria();
		criteria.createAlias("events", "eventsAlias");
		criteria.add(Restrictions.eq("eventsAlias.id", event.getId()));
		return (User) criteria.uniqueResult();
	}

}
