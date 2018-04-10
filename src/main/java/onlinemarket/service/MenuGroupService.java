package onlinemarket.service;

import onlinemarket.model.MenuGroup;
import onlinemarket.util.exception.menuGroup.MenuGroupNotFoundException;

import java.util.List;

public interface MenuGroupService{
	MenuGroup getByKey(Integer key);

	List<MenuGroup> list();

	void update(MenuGroup menuGroup) throws MenuGroupNotFoundException;

	void save(MenuGroup menuGroup);

	void delete(Integer key) throws MenuGroupNotFoundException;
}
