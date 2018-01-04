package onlinemarket.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.RoleDao;
import onlinemarket.model.Role;

@Service("roleService")
@Transactional(readOnly=true)
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleDao roleDao;
	
	@Override
	public Role getByKey(Integer key) {
		return roleDao.getByKey(key);
	}

}
