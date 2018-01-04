package onlinemarket.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.RoleDao;
import onlinemarket.dao.UserDao;
import onlinemarket.model.Address;
import onlinemarket.model.Role;
import onlinemarket.model.State;
import onlinemarket.model.User;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{
 
    @Autowired
    private UserDao userDao;
	
	@Autowired
	RoleDao roleDao;
    
    @Autowired
    private PasswordEncoder passwordEndcoder;
    
    protected User beforeSave(User entity) {
    	
		if(entity.getState() == null)
		entity.setState(State.ACTIVE.getState());
		if(entity.getRoles().isEmpty()) {
			HashSet<Role> roles = new HashSet<>();
			Role role = roleDao.getByKey(2);
			roles.add(role);
			entity.setRoles(roles);
		}
		entity.setPassword(passwordEndcoder.encode(entity.getPassword()));
		return entity;
    }

	@Override
	public void save(User entity) {

		userDao.persist(entity);
	}

	@Override
	public void update(User entity) {
		userDao.update(entity);
	}

	@Override
	public void delete(User entity) {
		userDao.update(entity);
	}

	@Override
	public User getByKey(Integer key) {
		return userDao.getByKey(key);
	}

	@Override
	public List<User> list() {
		return userDao.list();
	}

	@Override
	public List<User> list(Integer offset, Integer maxResults) {
		return userDao.list(offset, maxResults);
	}

	@Override
	@Transactional(readOnly=true)
	public User getByEmail(String email) {
		return userDao.getByEmail(email);
	}

	@Override
	public void register(User user, Address address) {
		beforeSave(user);
		address.setUser(user);
		if (address.getLastName() == null && address.getFirstName() == null) {
			address.setFirstName(user.getFirstName());
			address.setLastName(user.getLastName());
		}
		HashSet<Address> addressList = new HashSet<>();
		addressList.add(address);
		user.setAddresses(addressList);
		userDao.persist(user);
	}

	@Override
	public User getByDeclaration(String key, String value) {
		// TODO Auto-generated method stub
		return null;
	}

}
