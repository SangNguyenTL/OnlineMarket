package onlinemarket.dao;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Event;
import onlinemarket.model.User;
import onlinemarket.result.ResultObject;

public interface UserDao extends InterfaceDao<Integer, User> {

    User getByEmail(String email);

    User getByEvent(Event event);

}
