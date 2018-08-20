package OnlineMarket.service;

import OnlineMarket.model.Role;

import java.util.List;

public interface RoleService {
	
	Role getByKey(Integer key);

	List<Role> list();
	
}
