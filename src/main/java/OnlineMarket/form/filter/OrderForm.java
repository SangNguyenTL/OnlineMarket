package OnlineMarket.form.filter;

import OnlineMarket.model.Event;
import OnlineMarket.model.Order;
import OnlineMarket.model.OrderDetail;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class OrderForm extends Order {

    private List<Event> eventList = new ArrayList<>(0);

    private List<OrderDetail> orderDetailList = new ArrayList<>(0);

    public OrderForm(Order order) {
        for (Method getMethod : order.getClass().getMethods()) {
            if (getMethod.getName().startsWith("get")) {
                try {
                    Method setMethod = this.getClass().getMethod(getMethod.getName().replace("get", "set"), getMethod.getReturnType());
                    setMethod.invoke(this, getMethod.invoke(order, (Object[]) null));

                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ignore) {

                }
            }
        }
    }

    public OrderForm() {

    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    @Valid
    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }
}
