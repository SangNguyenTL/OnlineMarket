package OnlineMarket.dao;

import OnlineMarket.model.Role;

import java.util.List;

public interface RoleDao {
	
	Role getByKey(Integer key);

	List<Role> list();
}
