package onlinemarket.service;

import java.util.List;

import onlinemarket.util.exception.CustomException;
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
	public void save(Event entity) {
		eventDao.save(entity);
	}

	@Override
	public void update(Event entity) throws CustomException {
		eventDao.update(entity);
	}

	@Override
	public void delete(Event entity) {
		eventDao.delete(entity);
	}

	@Override
	public Event getByKey(Integer key) {
		return eventDao.getByKey(key);
	}

	@Override
	public Event getByDeclaration(String key, String value) {
		return eventDao.getByDeclaration(key, value);
	}

	@Override
	public List<Event> list() {
		return eventDao.list();
	}

	@Override
	public List<Event> list(Integer offset, Integer maxResults) {
		return eventDao.list(offset, maxResults);
	}

	@Override
	public Event getByBrand(Brand brand) {
		return eventDao.getByBrand(brand);
	}

	@Override
	public Event getByProductCategory(ProductCategory productCategoryCheck) {
		return eventDao.getByProductCategory(productCategoryCheck);
	}

	@Override
	public ResultObject<Event> list(FilterForm filterForm) {
		return eventDao.list(filterForm);
	}

}
