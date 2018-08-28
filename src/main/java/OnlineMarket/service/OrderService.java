package OnlineMarket.service;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.form.filter.OrderForm;
import OnlineMarket.model.Event;
import OnlineMarket.model.Order;
import OnlineMarket.model.User;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.exception.CustomException;

import java.util.Map;

public interface OrderService{
    Order save(OrderForm entity);

    void update(Order entity);

    void delete(Order entity) throws CustomException;

    Order getByKey(Integer key);

    Order getByDeclaration(String key, Object value);

    Order getByEvent(Event event);

    ResultObject<Order> list(FilterForm filterForm);

    void delete(Integer id) throws CustomException;

    Order getByCodeAndUser(String code, User user);

    Order changeStatus(Integer id, Byte status) throws CustomException;

    Map<String, Long> count();

    ResultObject<Order> listByUserKey(User user, FilterForm filterForm);
}
