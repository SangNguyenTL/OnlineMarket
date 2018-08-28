package OnlineMarket.service;

import java.util.Date;

import OnlineMarket.dao.OrderDao;
import OnlineMarket.model.Order;
import OnlineMarket.model.User;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.exception.event.EventNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import OnlineMarket.dao.EventDao;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Event;
import OnlineMarket.result.ResultObject;

@Service("eventService")
@Transactional
public class EventServiceImpl implements EventService{

	@Autowired
	EventDao eventDao;

	@Autowired
	OrderDao orderDao;

	@Override
	public void save(Event event, User user) {
		event.setCreateDate(new Date());
		event.setPublisher(user);
		eventDao.save(event);
	}

	@Override
	public void update(Event event, User user) throws EventNotFoundException {
		Event event1 = eventDao.getByKey(event.getId());
		if(event1 == null) throw new EventNotFoundException();
		event.setPublisher(user);
		event.setCreateDate(event1.getCreateDate());
		event.setUpdateDate(new Date());
		eventDao.merge(event);
	}

	@Override
	public void delete(Integer id) throws EventNotFoundException, CustomException {
		Event event = eventDao.getByKey(id);
		if(event == null) throw new EventNotFoundException();
		Order order = orderDao.getByEvent(event);
		if(order != null) throw new CustomException("The event has order.");
		eventDao.delete(event);
	}

	@Override
	public Event getByDeclaration(String key, Object value) {
		return eventDao.getByDeclaration(key, value);
	}

	@Override
	public Event getByKey(Integer key) {
		return eventDao.getByKey(key);
	}

	@Override
	public ResultObject<Event> list(FilterForm filterForm) {
		return eventDao.list(filterForm);
	}

}
