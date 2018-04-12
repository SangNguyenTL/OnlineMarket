package onlinemarket.dao;

import onlinemarket.model.Menu;
import onlinemarket.model.MenuGroup;

import java.util.List;

public interface MenuDao extends InterfaceDao<Integer, Menu>{

    List<Menu> listByMenuGroup(MenuGroup menuGroup);

}
