package onlinemarket.service;

import java.util.Date;
import java.util.List;

import onlinemarket.model.User;
import onlinemarket.util.exception.CustomException;
import onlinemarket.util.exception.event.EventNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.EventDao;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.model.ProductCategory;
import onlinemarket.result.ResultObject;

@Service("eventService")
@Transactional
public class EventServiceImpl implements EventService{

	@Autowired
	EventDao eventDao;

	@Override
	public void save(Event event, User user) {
		event.setCreateDate(new Date());
		event.setPublisher(user);
		eventDao.save(event);
	}

	@Override
	public void update(Event event, User user) throws EventNotFoundException {
		Event event1 = eventDao.getByKey(user.getId());
		if(event1 == null) throw new EventNotFoundException();
		event1.updateEvent(event);
		eventDao.update(event1);
	}

	@Override
	public void delete(Integer id) throws EventNotFoundException {
		Event event = eventDao.getByKey(id);
		if(event == null) throw new EventNotFoundException();
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
