package onlinemarket.dao;

import onlinemarket.model.Event;
import onlinemarket.model.User;

public interface UserDao extends InterfaceDao<Integer, User>{
	
	public User getByEmail(String email);

	public User getByEvent(Event event);
	
}
