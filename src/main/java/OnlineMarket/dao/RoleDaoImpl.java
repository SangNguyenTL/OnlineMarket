package OnlineMarket.dao;

import org.springframework.stereotype.Repository;

import OnlineMarket.model.Role;

@Repository("roleDao")
public class RoleDaoImpl extends AbstractDao<Integer, Role> implements RoleDao {

}
