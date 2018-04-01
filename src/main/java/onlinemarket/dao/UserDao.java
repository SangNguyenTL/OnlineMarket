package onlinemarket.dao;

import onlinemarket.model.Event;
import onlinemarket.model.User;

public interface UserDao extends InterfaceDao<Integer, User> {

    User getByEmail(String email);

    User getByEvent(Event event);

}
