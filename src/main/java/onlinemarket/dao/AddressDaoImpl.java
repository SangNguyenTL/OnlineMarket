package onlinemarket.dao;

import org.springframework.stereotype.Repository;

import onlinemarket.model.Address;

@Repository("addressDao")
public class AddressDaoImpl extends AbstractDao<Integer, Address> implements AddressDao{

}
