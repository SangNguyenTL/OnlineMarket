package onlinemarket.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import onlinemarket.util.exception.CustomException;
import onlinemarket.util.exception.user.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.RoleDao;
import onlinemarket.dao.UserDao;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Address;
import onlinemarket.model.Event;
import onlinemarket.model.Role;
import onlinemarket.model.State;
import onlinemarket.model.User;
import onlinemarket.result.ResultObject;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	RoleDao roleDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private User beforeRegister(User entity) {
		if (entity.getState() == null)
			entity.setState(State.ACTIVE.getState());
		if (entity.getRole() == null) {
			Role role = roleDao.getByKey(1);
			entity.setRole(role);
		}
		if(!entity.getAddresses().isEmpty()){
			entity.getAddresses().get(0).setFirstName(entity.getFirstName());
			entity.getAddresses().get(0).setLastName(entity.getLastName());
		}
		entity.setCreateDate(new Date());
		entity.setPassword(passwordEncoder.encode(entity.getPassword()));
		return entity;
	}

	@Override
	public void save(User entity) {
		userDao.persist(beforeRegister(entity));
	}

	@Override
	public void update(User entity) throws CustomException{
		User user = userDao.getByKey(entity.getId());
		if(user == null) throw new CustomException("User not found");
		if(StringUtils.isNotBlank(entity.getPassword()))
			entity.setPassword(passwordEncoder.encode(entity.getPassword()));
		else
			entity.setPassword(user.getPassword());
		entity.setCreateDate(user.getCreateDate());
		entity.setUpdateDate(new Date());
		userDao.update(entity);
	}

	@Override
	public void delete(User entity) {
		userDao.delete(entity);
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
	@Transactional(readOnly = true)
	public User getByEmail(String email) {
		return userDao.getByEmail(email);
	}

	@Override
	public User getByDeclaration(String key, String value) {
		return userDao.getByDeclaration(key, value);
	}

	@Override
	public User getByEvent(Event event) {
		return userDao.getByEvent(event);
	}

	@Override
	public ResultObject<User> list(FilterForm filterForm) {
		return userDao.list(filterForm);
	}

	@Override
	public void delete(Integer id) throws UserNotFoundException, UserHasEventException, UserHasPostException, UserHasProductException, UserIsSuperAdminException {
		User user = userDao.getByKey(id);
		if(user == null) throw new UserNotFoundException();
		if(user.getId() == 1) throw new UserIsSuperAdminException();
		User user1 = userDao.getUniqueResultBy("posts.user", user);
		if(user1 !=null) throw new UserHasPostException("User has post "+user1.getPosts().iterator().next().getTitle());
		User user2 = userDao.getUniqueResultBy("events.user", user);
		if(user2 != null) throw new UserHasEventException("User has event "+user2.getEvents().iterator().next().getName());
		User user3 = userDao.getUniqueResultBy("products.user", user);
		if(user3 != null) throw new UserHasProductException("User has product "+user3.getProducts().iterator().next().getName());

		userDao.delete(user);
	}

}
