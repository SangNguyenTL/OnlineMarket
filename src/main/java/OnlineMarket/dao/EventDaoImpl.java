package OnlineMarket.dao;

import org.springframework.stereotype.Repository;

import OnlineMarket.model.Event;

@Repository("eventDao")
public class EventDaoImpl extends AbstractDao<Integer, Event> implements EventDao {
}
