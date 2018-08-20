package OnlineMarket.service;

import java.util.List;

import OnlineMarket.dao.OrderDao;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.exception.AddressNotFoundException;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.exception.address.AddressHasOrderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import OnlineMarket.dao.AddressDao;
import OnlineMarket.model.Address;
import OnlineMarket.model.Province;
import OnlineMarket.model.User;

@Service("addressService")
@Transactional
public class AddressServiceImpl implements AddressService  {

	@Autowired
	AddressDao addressDao;

	@Autowired
	OrderDao orderDao;
	
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
		addressDao.save(address);
	}

	@Override
	public Address getByDeclaration(String key, Object value) {
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
	public void delete(Integer id) throws AddressHasOrderException, AddressNotFoundException {
		Address address = addressDao.getByKey(id);
		if(address == null) throw new AddressNotFoundException();
		if(orderDao.getUniqueResultBy("address", address) != null) throw new AddressHasOrderException();
		delete(address);
	}

	@Override
	public void update(Address address, User user) throws CustomException, AddressNotFoundException {
		Address addressCheck = addressDao.getByKey(address.getId());
		if(addressCheck == null) throw new AddressNotFoundException();
		if(user == null) throw new CustomException("User not found");
		if(!user.equals(addressCheck.getUser())) throw new CustomException("The user is out of sync with the address");
		address.setUser(user);
		addressDao.merge(address);
	}
}
