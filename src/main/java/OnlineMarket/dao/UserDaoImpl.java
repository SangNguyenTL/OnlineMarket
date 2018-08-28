package OnlineMarket.dao;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.result.ResultObject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import OnlineMarket.model.Event;
import OnlineMarket.model.User;

import java.util.Collection;
import java.util.List;

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

	@Override
	public User getByEvent(Event event) {
		Criteria criteria = createEntityCriteria();
		criteria.createAlias("events", "eventsAlias");
		criteria.add(Restrictions.eq("eventsAlias.id", event.getId()));
		return (User) criteria.uniqueResult();
	}

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listAdmin() {
		Criteria criteria = createEntityCriteria();
		criteria.createAlias("role", "roleAlias");
		criteria.add(Restrictions.ne("roleAlias.name","USER"));
		return criteria.list();
    }

    @Override
    public ResultObject<User> list(FilterForm filterForm) {
        Criteria criteria = createEntityCriteria();
        criteria.createAlias("role", "roleAlias");
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if(authorities.contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"))) return childFilterFrom(criteria, filterForm);
        else if(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            criteria.add(Restrictions.eq("roleAlias.name", "USER"));
            return childFilterFrom(criteria, filterForm);
        }
        else return new ResultObject<>();
	}
}
