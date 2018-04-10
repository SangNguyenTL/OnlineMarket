package onlinemarket.service;

import java.util.List;

import javax.transaction.Transactional;

import onlinemarket.util.exception.CustomException;
import onlinemarket.util.exception.menuGroup.MenuGroupNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import onlinemarket.dao.MenuGroupDao;
import onlinemarket.model.MenuGroup;

@Service("menuGroupService")
@Transactional
public class MenuGroupServiceImpl implements MenuGroupService{

	@Autowired
	MenuGroupDao menuGroupDao;
	
	@Override
	public void save(MenuGroup entity) {
		menuGroupDao.save(entity);
	}

	@Override
	public void update(MenuGroup entity) throws MenuGroupNotFoundException {
		MenuGroup menuGroup = menuGroupDao.getByKey(entity.getId());
		if(menuGroup == null) throw new MenuGroupNotFoundException();
		menuGroupDao.update(entity);
	}

	@Override
	public void delete(Integer key) throws MenuGroupNotFoundException {

		MenuGroup menuGroup = menuGroupDao.getByKey(key);
		if(menuGroup == null) throw new MenuGroupNotFoundException();
		menuGroupDao.delete(menuGroup);

	}

	@Override
	public MenuGroup getByKey(Integer key) {
		return menuGroupDao.getByKey(key);
	}

	@Override
	public List<MenuGroup> list() {
		return menuGroupDao.list();
	}

}
