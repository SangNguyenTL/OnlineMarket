package onlinemarket.dao;


import onlinemarket.model.Address;
import onlinemarket.model.Province;

public interface AddressDao extends InterfaceDao<Integer ,Address>{

	Address getByProvince(Province province);
	
}
