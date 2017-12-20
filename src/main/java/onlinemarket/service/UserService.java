package onlinemarket.service;

import onlinemarket.model.User;

public interface UserService {
	
	User findById(int id);

	User findBySso(String sso);
}
