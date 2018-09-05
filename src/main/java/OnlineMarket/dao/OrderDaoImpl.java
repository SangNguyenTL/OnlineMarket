package OnlineMarket.dao;

import OnlineMarket.model.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import OnlineMarket.model.Event;
import OnlineMarket.model.Order;

import java.util.List;

@Repository("orderDao")
public class OrderDaoImpl extends AbstractDao<Integer, Order> implements OrderDao{

	@Override
	@SuppressWarnings("unchecked")
	public Order getByEvent(Event event) {
		Criteria criteria = createEntityCriteria();
		criteria.createAlias("events", "eventsAlias");
		criteria.add(Restrictions.eq("eventsAlias.id", event.getId()));
		List<Order> order = criteria.list();
		return order != null && !order.isEmpty() ? order.get(0): null;
	}

    @Override
    public Order getByCodeAndUser(String code, User user) {

		Criteria criteria = createEntityCriteria();
		criteria.createAlias("events", "eventsAlias");
		criteria.add(Restrictions.eq("eventsAlias.code", code));
		criteria.add(Restrictions.eq("user", user));
        return (Order) criteria.uniqueResult();
    }

}
