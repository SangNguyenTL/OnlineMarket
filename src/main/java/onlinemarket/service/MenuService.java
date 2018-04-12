package onlinemarket.service;

import java.util.List;

import onlinemarket.model.Menu;
import onlinemarket.model.MenuGroup;
import onlinemarket.util.exception.menu.MenuNotFoundException;
import onlinemarket.util.exception.menuGroup.MenuGroupNotFoundException;

public interface MenuService{

	void save(Menu menu) throws MenuNotFoundException, MenuGroupNotFoundException;

	void update(Menu menu) throws MenuNotFoundException, MenuGroupNotFoundException;

	void delete(Integer menu);

	List<Menu> list(MenuGroup menuGroup);

}
