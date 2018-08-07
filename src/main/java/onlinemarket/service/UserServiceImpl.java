package onlinemarket.service;

import java.security.Principal;
import java.util.*;

import onlinemarket.util.exception.CustomException;
import onlinemarket.util.exception.user.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.RoleDao;
import onlinemarket.dao.UserDao;
import onlinemarket.form.filter.FilterForm;
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

	@Autowired
	private SessionRegistry sessionRegistry;

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
			entity.getAddresses().get(0).setUser(entity);
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
		boolean flagReset = false;
		if(user == null) throw new CustomException("User not found");
		if(StringUtils.isNotBlank(entity.getPassword())){
			flagReset = true;
			entity.setPassword(passwordEncoder.encode(entity.getPassword()));
		}
		else
			entity.setPassword(user.getPassword());
		if(!StringUtils.equals(user.getEmail(), entity.getEmail())) flagReset = true;
		entity.setCreateDate(user.getCreateDate());
		entity.setUpdateDate(new Date());

		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

		if(authorities.contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"))){
			if(!user.getRole().getId().equals(entity.getRole().getId())){
				entity.setRole(user.getRole());
				flagReset = true;
			}
		}

		User newUser = (User) userDao.merge(entity);
		if(flagReset)
		removeSession(newUser);
	}

	private void removeSession(User newUser) {
		if(newUser != null){
			String userName;
			List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
			for(Object principal : allPrincipals){

					UserDetails authentication = (UserDetails) principal;
					userName = authentication.getUsername();
					if(userName.equals(newUser.getEmail()))
					{
						List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, false);
						if(null != sessionsInfo && sessionsInfo.size() > 0) {
							for (SessionInformation sessionInformation : sessionsInfo) {
								sessionInformation.expireNow();
								sessionRegistry.removeSessionInformation(sessionInformation.getSessionId());

							}
						}
						return;
					}
			}
		}
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
		User userTest;
		if(user == null) throw new UserNotFoundException();
		if(user.getRole().getName() == "SUPER_ADMIN") throw new UserIsSuperAdminException();
		userTest = userDao.getUniqueResultBy("posts.user", user);
		if(userTest !=null) throw new UserHasPostException("User has post "+user.getPosts().iterator().next().getTitle());
		userTest = userDao.getUniqueResultBy("products.user", user);
		if(userTest != null) throw new UserHasProductException("User has product "+user.getProducts().iterator().next().getName());
		userTest = userDao.getUniqueResultBy("events.publisher", user);
		if(userTest != null) throw new UserHasProductException("User has event "+user.getEvents().iterator().next().getName());

		userDao.delete(user);

		removeSession(user);
	}

	public User getCurrentUser() {
		String userName;
		User currentUser = new User();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication.isAuthenticated()) {
			Object principal =  authentication.getPrincipal();
			if(principal instanceof UserDetails){
				userName = ((UserDetails) authentication.getPrincipal()).getUsername();
				currentUser = getByEmail(userName);
			}
		}

		return currentUser;
	}

	@Override
	public User getByKeyAndRole(int key) {
		User user = getByKey(key);
		User currentUser = getCurrentUser();
		switch (currentUser.getRole().getName()){
			case "SUPER_ADMIN":
				return user;
			case "ADMIN":
				if((user.getRole().equals("ADMIN") && user.getId() == currentUser.getId()) || user.getRole().getName().equals("USER")) return user;
			case "USER":
				if( user.getId() == currentUser.getId()) return user;
			default:
		}
		return null;
	}

}
