package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Address;
import onlinemarket.model.Event;
import onlinemarket.model.User;
import onlinemarket.result.ResultObject;

public interface UserService extends InterfaceService<Integer, User> {

    User getByEmail(String email);

    User getByEvent(Event event);

    void register(User user, Address address);

    ResultObject<User> list(FilterForm filterForm);

}
