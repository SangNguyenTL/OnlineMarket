package OnlineMarket.listener;

import OnlineMarket.model.Notification;
import OnlineMarket.model.Order;
import OnlineMarket.model.User;
import OnlineMarket.service.NotificationService;
import OnlineMarket.service.SendMailConstruction;
import OnlineMarket.service.UserService;
import OnlineMarket.service.config.ConfigurationService;
import OnlineMarket.util.exception.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Objects;

@Component
public class OrderListener implements ApplicationListener<OnOrderEvent> {

    @Autowired
    UserService userService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    SendMailConstruction sendMailConstruction;

    @Override
    public void onApplicationEvent(final OnOrderEvent event) {
        final String action = event.getAction();
        final Order order = event.getOder();
        switch (action){
            case "submit":
                submitOrder(order, event);
                break;
            case "delete":
                deleteOrder(order,event);
                break;
            case "approved":
                confirmOrder(order, event);
                break;
        }
    }

    private void submitOrder(Order order,final OnOrderEvent event) {
        List<User> list = userService.listAdmin();
        String contextPath = "";
        if(event.getRequest() != null)
            contextPath = event.getRequest().getContextPath();
        for(User user : list){
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setContent("Your system was received a order. Please check that in <a href='"+contextPath+"/admin/order/update/"+order.getId()+"'>this link</a>");
            try {
                notificationService.add(notification);
            } catch (UserNotFoundException ignore) {
            }
        }
    }

    private void deleteOrder(Order order, OnOrderEvent event){
        User currentUser = userService.getCurrentUser();
        User user = order.getUser();
        if(Objects.equals(user.getId(), currentUser.getId()))return;

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setContent("Your order has id #"+order.getId()+" wasn't approved and deleted by system manager");
        try {
            notificationService.add(notification);
            sendMailConstruction.sendDeleteOrderToUser(notification, event.getRequest());
        } catch (MessagingException|UserNotFoundException ignore) {
        }
    }

    private void confirmOrder(Order order, OnOrderEvent event){
        User user = order.getUser();
        Notification notification = new Notification();
        notification.setUser(user);
        String contextPath = "";
        if(event.getRequest() != null)
            contextPath = event.getRequest().getContextPath();
        notification.setContent("Your order has id #"+order.getId()+" was approved<br/> View info in <a href='"+contextPath+"/user/"+user.getId()+"/order/view/"+order.getId()+"'>this link</a>");
        try {
            notificationService.add(notification);
            sendMailConstruction.sendApprovedOrderTotUser(order, user, event.getRequest());
        } catch (MessagingException|UserNotFoundException ignore) {
        }
    }
}
