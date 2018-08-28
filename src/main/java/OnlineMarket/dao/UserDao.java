package OnlineMarket.dao;

import OnlineMarket.model.Event;
import OnlineMarket.model.User;

import java.util.List;

public interface UserDao extends InterfaceDao<Integer, User> {

    User getByEmail(String email);

    User getByEvent(Event event);

    List listAdmin();
}
