package onlinemarket.service;

import java.util.List;

import onlinemarket.model.Menu;
import onlinemarket.model.MenuGroup;

public interface MenuService extends InterfaceService<Integer, Menu>{

	List<Menu> listByDeclaration(String propertyName, MenuGroup menuGroup);

}
