package onlinemarket.service;

import java.util.List;

import javax.transaction.Transactional;

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
	public void update(MenuGroup entity) {
		menuGroupDao.update(entity);
	}

	@Override
	public void delete(MenuGroup entity) {
		menuGroupDao.delete(entity);
	}

	@Override
	public MenuGroup getByKey(Integer key) {
		return menuGroupDao.getByKey(key);
	}

	@Override
	public MenuGroup getByDeclaration(String key, String value) {
		return menuGroupDao.getByDeclaration(key, value);
	}

	@Override
	public List<MenuGroup> list() {
		return menuGroupDao.list();
	}

	@Override
	public List<MenuGroup> list(Integer offset, Integer maxResults) {
		return menuGroupDao.list(offset, maxResults);
	}

}
