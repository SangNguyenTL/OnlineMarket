package onlinemarket.service;

import java.util.List;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.result.ResultObject;
import onlinemarket.util.exception.AddressNotFoundException;
import onlinemarket.util.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.AddressDao;
import onlinemarket.model.Address;
import onlinemarket.model.Province;
import onlinemarket.model.User;

@Service("addressService")
@Transactional
public class AddressServiceImpl implements AddressService  {

	@Autowired
	AddressDao addressDao;

	
	@Override
	public void save(Address address) {
		addressDao.persist(address);
	}

	@Override
	public void update(Address address) throws CustomException {
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
	public void save(Address address, User user) throws CustomException{
		if(user == null) throw new CustomException("User not found");
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

	@Override
	public ResultObject<Address> listByUser(User user, FilterForm filterForm) throws CustomException{
		if(user == null) throw new CustomException("User not found");
		if(filterForm == null) throw new CustomException("Filter error.");
		return addressDao.listByUser(user, filterForm);
	}

	@Override
	public void update(Address address, User user) throws CustomException, AddressNotFoundException {
		Address addressCheck = addressDao.getByKey(address.getId());
		if(addressCheck == null) throw new AddressNotFoundException();
		if(user == null) throw new CustomException("User not found");
		if(!user.equals(addressCheck.getUser())) throw new CustomException("The user is out of sync with the address");
		address.setUser(user);
		addressDao.update(address);
	}
}
