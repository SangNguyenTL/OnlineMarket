package OnlineMarket.service;

import OnlineMarket.dao.EventDao;
import OnlineMarket.dao.ProductDao;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.form.filter.OrderForm;
import OnlineMarket.listener.OnOrderEvent;
import OnlineMarket.model.*;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.other.EventStatus;
import OnlineMarket.util.other.OrderStatus;
import OnlineMarket.util.other.ProductStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import OnlineMarket.dao.OrderDao;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService{

	@Autowired
	OrderDao orderDao;

	@Autowired
    EventDao eventDao;

	@Autowired
    ProductDao productDao;

	@Autowired
    ApplicationEventPublisher eventPublisher;

	@Autowired
    HttpServletRequest request;
	
	@Override
	public Order save(OrderForm orderForm) {
	    Order order = new Order(orderForm);
        order.setStatus(OrderStatus.WAITING.getId());
        order.setCreateDate(new Date());

        orderDao.persist(order);

	    for(Event event : orderForm.getEventList()){
	        if(event.getCount() != null) {
	            event.setCount(event.getCount() - 1);
                if( event.getCount() == 0){
                    event.setStatus(EventStatus.INACTIVE.getId());
                }
                eventDao.merge(event);
            }
            event.getOrders().add(order);
            order.getEvents().add(event);
        }
        for(OrderDetail orderDetail : orderForm.getOrderDetailList()){
	        Product product = productDao.getByKey(orderDetail.getProduct().getId());
            if (product != null) {
                product.setQuantity(product.getQuantity() - orderDetail.getProductQuantity());
                if(product.getQuantity() == 0) product.setState(ProductStatus.OUTOFSTOCK.getId());
                product.setNumberOrder(product.getNumberOrder()+orderDetail.getProductQuantity());
                productDao.merge(product);
            }
            orderDetail.setOrder(order);
            orderDetail.getId().setOrderId(order.getId());
            order.getOrderDetails().add(orderDetail);
        }
        orderDao.merge(order);
	    return order;
	}

	@Override
	public void update(Order entity) {
		orderDao.update(entity);
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

    @Override
    public ResultObject<Order> list(FilterForm filterForm) {
        return orderDao.list(filterForm);
    }

    @Override
    public void delete(Integer id) throws CustomException {
        Order order = orderDao.getByKey(id);
        delete(order);
    }

    @Override
    public void delete(Order order) throws CustomException {
        if(order == null) throw new CustomException("Order not found.");
        if(order.getStatus().equals(OrderStatus.DELIVERING.getId())) throw new CustomException("You can not delete an order that is in the process of delivery.");
        if(Objects.equals(order.getStatus(), OrderStatus.WAITING.getId())){
            for(OrderDetail orderDetail : order.getOrderDetails()){
                orderDetail.setOrder(order);
                Product product = orderDetail.getProduct();
                if(product.getQuantity() == 0) {
                    product.setState(ProductStatus.INSTOCK.getId());
                }
                product.setQuantity(product.getQuantity() + orderDetail.getProductQuantity());
                product.setNumberOrder(product.getNumberOrder()-orderDetail.getProductQuantity());
                productDao.merge(product);
            }
            eventPublisher.publishEvent(new OnOrderEvent(order, "delete", request));
        }
        orderDao.delete(order);
    }

    @Override
    public Order getByCodeAndUser(String code, User user){
	    return orderDao.getByCodeAndUser(code, user);
    }

    @Override
    public Order changeStatus(Integer id, Byte status) throws CustomException {
        if(status < 0 || status > 2) throw new CustomException("The status is invalid");
        Order order = orderDao.getByKey(id);
        if(order == null) throw new CustomException("Order not found.");
        Byte currentStatus = order.getStatus();
        if(status < currentStatus || status >= currentStatus+2 || status == currentStatus) throw new CustomException("The status must be advanced, not retrograde.");
        if(Objects.equals(status, OrderStatus.DELIVERING.getId())) order.setUpdateDate(new Date());
        if(Objects.equals(status, OrderStatus.COMPLETE.getId())) order.setInvoiceDate(new Date());
        order.setStatus(status);
        orderDao.merge(order);
	    return order;
    }

    @Override
    public Map<String, Long> count(){
	    Map<String, Long> map = new HashMap<>();
	    long wait = orderDao.countBy("status", OrderStatus.WAITING.getId());
	    long del = orderDao.countBy("status", OrderStatus.DELIVERING.getId());
	    long com = orderDao.countBy("status", OrderStatus.COMPLETE.getId());
	    long all = wait+del+com;
	    map.put("waiting", wait);
	    map.put("delivering", del);
	    map.put("complete", com);
	    map.put("all", all);
	    return map;
    }

    @Override
    public ResultObject<Order> listByUserKey(User user, FilterForm filterForm) {
        return orderDao.listByDeclaration("user", user, filterForm);
    }
}
