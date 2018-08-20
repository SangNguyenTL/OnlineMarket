package OnlineMarket.service;

import java.util.List;

import OnlineMarket.model.Menu;
import OnlineMarket.model.MenuGroup;
import OnlineMarket.util.exception.menu.MenuNotFoundException;
import OnlineMarket.util.exception.menuGroup.MenuGroupNotFoundException;

public interface MenuService{

	void save(Menu menu) throws MenuNotFoundException, MenuGroupNotFoundException;

	void update(Menu menu) throws MenuNotFoundException, MenuGroupNotFoundException;

	void delete(Integer menu);

	List<Menu> list(MenuGroup menuGroup);

}
