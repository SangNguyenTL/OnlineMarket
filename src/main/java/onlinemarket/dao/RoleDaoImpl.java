package onlinemarket.dao;

import org.springframework.stereotype.Repository;

import onlinemarket.model.Role;

@Repository("roleDao")
public class RoleDaoImpl extends AbstractDao<Integer, Role> implements RoleDao {

}
