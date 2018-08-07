package onlinemarket.dao;

import org.springframework.stereotype.Repository;

import onlinemarket.model.Event;

@Repository("eventDao")
public class EventDaoImpl extends AbstractDao<Integer, Event> implements EventDao {
}
