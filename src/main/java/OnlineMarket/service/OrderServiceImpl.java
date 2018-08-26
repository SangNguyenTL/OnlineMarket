package OnlineMarket.service;

import OnlineMarket.dao.EventDao;
import OnlineMarket.dao.ProductDao;
import OnlineMarket.model.OrderDetail;
import OnlineMarket.model.Product;
import OnlineMarket.util.other.EventStatus;
import OnlineMarket.util.other.OrderStatus;
import OnlineMarket.util.other.ProductStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import OnlineMarket.dao.OrderDao;
import OnlineMarket.model.Event;
import OnlineMarket.model.Order;

import java.util.Date;

@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService{

	@Autowired
	OrderDao orderDao;

	@Autowired
    EventDao eventDao;

	@Autowired
    ProductDao productDao;
	
	@Override
	public void save(Order order) {
        order.setStatus(OrderStatus.WAITING.getId());
        order.setCreateDate(new Date());
	    for(Event event : order.getEvents()){
	        if(event.getCount() != null) {
	            event.setCount(event.getCount() - 1);
                if( event.getCount() == 0){
                    event.setStatus(EventStatus.INACTIVE.getId());
                }
                eventDao.merge(event);
            }
        }
        for(OrderDetail orderDetail : order.getOrderDetails()){
	        Product product = productDao.getByKey(orderDetail.getProduct().getId());
	        product.setQuantity(product.getQuantity() - orderDetail.getProductQuantity());
	        if(product.getQuantity() == 0) product.setState(ProductStatus.OUTOFSTOCK.getId());
	        productDao.merge(product);
        }
		orderDao.persist(order);
	}

	@Override
	public void update(Order entity) {
		orderDao.update(entity);
	}

	@Override
	public void delete(Order entity) {
		orderDao.delete(entity);
	}

	@Override
	public Order getByKey(Integer key) {
		return orderDao.getByKey(key);
	}

	@Override
	public Order getByDeclaration(String key, Object value) {
		return orderDao.getByDeclaration(key, value);
	}

	@Override
	public Order getByEvent(Event event) {
		return orderDao.getByEvent(event);
	}

}
