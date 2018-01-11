package onlinemarket.service;

import onlinemarket.model.Address;
import onlinemarket.model.Event;
import onlinemarket.model.User;

public interface UserService extends InterfaceService<Integer, User>{
	
	public User getByEmail(String email);
	
	public User getByEvent(Event event);
	
	public void register(User user, Address address);

}
