package onlinemarket.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import onlinemarket.model.User;

@Repository("userDao")
public class UserDaoImpl extends AbstractDao<Integer, User> implements UserDao {

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

}
