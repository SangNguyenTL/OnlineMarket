package OnlineMarket.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import OnlineMarket.dao.RoleDao;
import OnlineMarket.model.Role;

import java.util.List;

@Service("roleService")
@Transactional(readOnly=true)
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleDao roleDao;
	
	@Override
	public Role getByKey(Integer key) {
		return roleDao.getByKey(key);
	}

	@Override
	public List<Role> list() {
		return roleDao.list();
	}

}
