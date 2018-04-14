package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Event;
import onlinemarket.model.User;
import onlinemarket.result.ResultObject;
import onlinemarket.util.exception.event.EventNotFoundException;

public interface EventService {

    void save(Event event, User user);

    void update(Event event, User user) throws EventNotFoundException;

    void delete(Integer id) throws EventNotFoundException;

    Event getByDeclaration(String key, Object value);

    Event getByKey(Integer key);

    ResultObject<Event> list(FilterForm filterForm);

}
