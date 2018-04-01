package onlinemarket.dao;

import onlinemarket.model.Role;

import java.util.List;

public interface RoleDao {
	
	Role getByKey(Integer key);

	List<Role> list();
}
