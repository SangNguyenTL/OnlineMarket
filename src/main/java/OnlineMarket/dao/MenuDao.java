package OnlineMarket.dao;

import OnlineMarket.model.Menu;
import OnlineMarket.model.MenuGroup;

import java.util.List;

public interface MenuDao extends InterfaceDao<Integer, Menu>{

    List<Menu> listByMenuGroup(MenuGroup menuGroup);

}
