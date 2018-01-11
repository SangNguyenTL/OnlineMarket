package onlinemarket.service;

import onlinemarket.model.Address;
import onlinemarket.model.Province;
import onlinemarket.model.User;

public interface AddressService extends InterfaceService<Integer, Address> {
	
	public void save(Address address, User user);

	public Address getByProvince(Province province);
	
}
