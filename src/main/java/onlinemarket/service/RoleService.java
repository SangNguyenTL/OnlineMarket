package onlinemarket.service;

import onlinemarket.model.Role;

import java.util.List;

public interface RoleService {
	
	Role getByKey(Integer key);

	List<Role> list();
	
}
