package onlinemarket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.AddressDao;
import onlinemarket.model.Address;
import onlinemarket.model.Province;
import onlinemarket.model.User;

@Service("addressService")
@Transactional
public class AddressServiceImpl implements AddressService {

	@Autowired
	AddressDao addressDao;

	
	@Override
	public void save(Address address) {
		addressDao.persist(address);
	}

	@Override
	public void update(Address address) {
		addressDao.update(address);
	}

	@Override
	public void delete(Address address) {
		addressDao.delete(address);
	}

	@Override
	public Address getByKey(Integer key) {
		return addressDao.getByKey(key);
	}

	@Override
	public List<Address> list() {
		return addressDao.list();
	}

	@Override
	public List<Address> list(Integer offset, Integer maxResults) {
		return addressDao.list(offset, maxResults);
	}

	@Override
	public void save(Address address, User user) {
		address.setUser(user);
		if (address.getLastName() == null && address.getFirstName() == null) {
			address.setFirstName(user.getFirstName());
			address.setLastName(user.getLastName());
		}
		addressDao.persist(address);
	}

	@Override
	public Address getByDeclaration(String key, String value) {
		return addressDao.getByDeclaration(key, value);
	}

	@Override
	public Address getByProvince(Province province) {
		return addressDao.getByProvince(province);
	}

}
