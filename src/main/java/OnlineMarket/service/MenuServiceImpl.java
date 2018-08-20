package OnlineMarket.service;

import java.util.List;

import javax.transaction.Transactional;

import OnlineMarket.dao.MenuGroupDao;
import OnlineMarket.util.exception.menu.MenuNotFoundException;
import OnlineMarket.util.exception.menuGroup.MenuGroupNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import OnlineMarket.dao.MenuDao;
import OnlineMarket.model.Menu;
import OnlineMarket.model.MenuGroup;

@Service("menuService")
@Transactional
public class MenuServiceImpl implements MenuService{

	@Autowired
	MenuDao menuDao;

	@Autowired
	MenuGroupDao menuGroupDao;

	@Override
	public void save(Menu entity) throws MenuNotFoundException, MenuGroupNotFoundException {
		addMenuParent(entity);
		MenuGroup menuGroup = menuGroupDao.getByKey(entity.getMenuGroupId());
		if(menuGroup == null) throw new MenuGroupNotFoundException();
		entity.setMenuGroup(menuGroup);
		menuDao.save(entity);
	}

	@Override
	public void update(Menu entity) throws MenuNotFoundException, MenuGroupNotFoundException {
		Menu menu = menuDao.getByKey(entity.getId());
		if(menu == null)  throw new MenuNotFoundException();
		addMenuParent(entity);
		MenuGroup menuGroup = menuGroupDao.getByKey(entity.getMenuGroupId());
		if(menuGroup == null) throw new MenuGroupNotFoundException();
		entity.setMenuGroup(menuGroup);
		menuDao.merge(entity);
	}

	private void addMenuParent(Menu entity) throws MenuNotFoundException {
		if(entity.getParentId() != null){
			Menu menuParent = menuDao.getByKey(entity.getParentId());
			if(menuParent == null) throw new MenuNotFoundException();
			entity.setMenu(menuParent);
		}
	}

	@Override
	public void delete(Integer id) {
		menuDao.delete(menuDao.getByKey(id));
	}

	@Override
	public List<Menu> list(MenuGroup menuGroup) {
		return menuDao.listByMenuGroup(menuGroup);
	}

}
