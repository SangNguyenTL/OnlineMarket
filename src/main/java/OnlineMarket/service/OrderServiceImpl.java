package OnlineMarket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import OnlineMarket.dao.OrderDao;
import OnlineMarket.model.Event;
import OnlineMarket.model.Order;

@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService{

	@Autowired
	OrderDao orderDao;
	
	@Override
	public void save(Order entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Order entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Order entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Order getByKey(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order getByDeclaration(String key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Order> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Order> list(Integer offset, Integer maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order getByEvent(Event event) {
		return orderDao.getByEvent(event);
	}

}
