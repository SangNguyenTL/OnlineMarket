package OnlineMarket.service;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Event;
import OnlineMarket.model.User;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.exception.event.EventNotFoundException;

public interface EventService {

    void save(Event event, User user);

    void update(Event event, User user) throws EventNotFoundException;

    void delete(Integer id) throws EventNotFoundException;

    Event getByDeclaration(String key, Object value);

    Event getByKey(Integer key);

    ResultObject<Event> list(FilterForm filterForm);

}
