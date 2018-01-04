package onlinemarket.service;

import onlinemarket.model.Address;
import onlinemarket.model.User;

public interface AddressService extends InterfaceService<Integer, Address> {
	
	public void save(Address address, User user);
	
}
