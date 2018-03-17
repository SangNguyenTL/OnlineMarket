package onlinemarket.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import onlinemarket.dao.MenuDao;
import onlinemarket.model.Menu;
import onlinemarket.model.MenuGroup;

@Service("menuService")
@Transactional
public class MenuServiceImpl implements MenuService{

	@Autowired
	MenuDao menuDao;
	
	@Override
	public void save(Menu entity) {
		menuDao.save(entity);
	}

	@Override
	public void update(Menu entity) {
		menuDao.update(entity);
	}

	@Override
	public void delete(Menu entity) {
		menuDao.delete(entity);
	}

	@Override
	public Menu getByKey(Integer key) {
		return menuDao.getByKey(key);
	}

	@Override
	public Menu getByDeclaration(String key, String value) {
		return menuDao.getByDeclaration(key, value);
	}

	@Override
	public List<Menu> list() {
		return menuDao.list();
	}

	@Override
	public List<Menu> list(Integer offset, Integer maxResults) {
		return menuDao.list(offset, maxResults);
	}

	@Override
	public List<Menu> listByDeclaration(String propertyName, MenuGroup menuGroup) {
		return menuDao.listByDeclaration(propertyName, menuGroup);
	}

}
