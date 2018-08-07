package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Address;
import onlinemarket.model.Event;
import onlinemarket.model.User;
import onlinemarket.result.ResultObject;
import onlinemarket.util.exception.user.*;

public interface UserService extends InterfaceService<Integer, User> {

    User getByEmail(String email);

    User getByEvent(Event event);

    ResultObject<User> list(FilterForm filterForm);

    void delete(Integer id) throws UserNotFoundException, UserHasEventException, UserHasPostException, UserHasProductException, UserIsSuperAdminException;

    User getCurrentUser();

    User getByKeyAndRole(int key);
}
