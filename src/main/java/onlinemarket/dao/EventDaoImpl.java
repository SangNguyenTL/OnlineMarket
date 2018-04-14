package onlinemarket.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.model.ProductCategory;
import onlinemarket.model.User;

@Repository("eventDao")
public class EventDaoImpl extends AbstractDao<Integer, Event> implements EventDao {
}
