package OnlineMarket.service;

import java.util.*;

import OnlineMarket.dao.*;
import OnlineMarket.model.*;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.exception.user.*;
import OnlineMarket.util.other.State;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.result.ResultObject;

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

	@Autowired
	VerificationTokenDao verificationTokenDao;

	@Autowired
	PasswordResetTokenDao passwordResetTokenDao;

    @Autowired
    PersistentTokenRepository tokenRepository;

    @Autowired
	RatingService ratingService;

    @Autowired
	RatingDao ratingDao;

	private User currentUser;

	@Override
	public void save(User entity) {
        if (entity.getState() == null)
            entity.setState(State.INACTIVE.name());
        if (entity.getRole() == null) {
            Role role = roleDao.getByKey(2);
            entity.setRole(role);
        }
        if(!entity.getAddresses().isEmpty()){
            entity.getAddresses().get(0).setFirstName(entity.getFirstName());
            entity.getAddresses().get(0).setLastName(entity.getLastName());
            entity.getAddresses().get(0).setUser(entity);
        }
        entity.setCreateDate(new Date());
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));

		userDao.persist(entity);
	}

	@Override
	public void update(User entity, boolean flagReset) throws CustomException{
		User user = userDao.getByKey(entity.getId());
		if(user == null) throw new CustomException("User not found");

		if(!StringUtils.equals(user.getPassword(), entity.getPassword())){
			entity.setPassword(passwordEncoder.encode(entity.getPassword()));
		}

		entity.setUpdateDate(new Date());

		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

		if(!authorities.contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN")))
			entity.setRole(null);

		user.merge(entity);

		userDao.merge(user);
		if(flagReset)
		removeSession(entity);
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
                                tokenRepository.removeUserTokens(userName);
                                sessionInformation.expireNow();
							}
						}
						return;
					}
			}
		}
	}


	@Override
	public User getByKey(Integer key) {
		return userDao.getByKey(key);
	}

	@Override
	@Transactional(readOnly = true)
	public User getByEmail(String email) {
		return userDao.getByEmail(email);
	}

	@Override
	public User getByDeclaration(String key, Object value) {
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
		if(user.getRole().getName().equals("SUPER_ADMIN")) throw new UserIsSuperAdminException();
		userTest = userDao.getUniqueResultBy("posts.user", user);
		if(userTest !=null) throw new UserHasPostException("User has post "+user.getPosts().iterator().next().getTitle());
		userTest = userDao.getUniqueResultBy("products.user", user);
		if(userTest != null) throw new UserHasProductException("User has product "+user.getProducts().iterator().next().getName());
		userTest = userDao.getUniqueResultBy("events.publisher", user);
		if(userTest != null) throw new UserHasEventException("User has event "+user.getEvents().iterator().next().getName());

		final VerificationToken verificationToken = verificationTokenDao.findByUser(user);

		if (verificationToken != null) {
			verificationTokenDao.delete(verificationToken);
		}

		List<Rating> ratings = ratingService.list(user);
		for(Rating rating : ratings){
            ratingService.delete(rating);
        }

		userDao.delete(user);

		removeSession(user);
	}

	public User getCurrentUser() {
		String userName;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			Object principal =  authentication.getPrincipal();
			if(principal instanceof UserDetails){
				userName = ((UserDetails) authentication.getPrincipal()).getUsername();
				if(currentUser == null || !StringUtils.equals(userName, currentUser.getEmail()))
					currentUser = getByEmail(userName);
			}else  currentUser = null;
		}else currentUser = null;

		return currentUser;
	}

    @Override
    public void saveOriginal(User user) {
        userDao.persist(user);
    }

    @Override
	public void createVerificationTokenForUser(User user, String token) {
		final VerificationToken myToken = new VerificationToken(token, user);
		verificationTokenDao.save(myToken);
	}

	@Override
	public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
		VerificationToken vToken = verificationTokenDao.findByToken(existingVerificationToken);
		vToken.updateToken(UUID.randomUUID().toString());
		vToken = verificationTokenDao.merge(vToken);
		return vToken;
	}

	@Override
	public VerificationToken getVerificationToken(final String VerificationToken) {
		return verificationTokenDao.findByToken(VerificationToken);
	}

	@Override
	public void createPasswordResetTokenForUser(User user, String token) {
		PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordResetTokenDao.save(myToken);
	}

	@Override
	public PasswordResetToken generateNewPasswordResetToken(String existingPasswordResetToken) {
		PasswordResetToken passwordResetToken = passwordResetTokenDao.getByDeclaration("token", existingPasswordResetToken);
		passwordResetToken.updateToken(existingPasswordResetToken);
		passwordResetToken = passwordResetTokenDao.merge(passwordResetToken);
		return passwordResetToken;
	}

	@Override
	public PasswordResetToken getPasswordResetToken(String passwordResetToken) {
		return passwordResetTokenDao.getByDeclaration("token", passwordResetToken);
	}

	@Override
	public PasswordResetToken getPasswordResetTokenByUser(User user) {
		return passwordResetTokenDao.getByDeclaration("user", user);
	}

	@Override
	public void deleteVerificationToken(VerificationToken verificationToken) {
		verificationTokenDao.delete(verificationToken);
	}

	@Override
	public void deletePasswordResetToken(PasswordResetToken passwordResetToken) {
		passwordResetTokenDao.delete(passwordResetToken);
	}

	@Override
	public Map<String,Long> countUser(){

		Map<String,Long> map = new HashMap<>();
		long active = userDao.countBy("state", State.ACTIVE.name());
		long inactive = userDao.countBy("state", State.INACTIVE.name());
		long lock = userDao.countBy("state", State.LOCKED.name());
		long deleted = userDao.countBy("state", State.DELETED.name());
		long all = userDao.count();

		map.put("active",active);
		map.put("inactive", inactive);
		map.put("lock", lock);
		map.put("deleted", deleted);
		map.put("all",all);

		return map;
	}

    @Override
    public List<User> listAdmin() {
        return userDao.listAdmin();
    }

    @Override
    public void changePass(User user, String password) throws CustomException {
        if(user == null) throw new CustomException("User not found");
        user.setPassword(passwordEncoder.encode(password));
    }
}
