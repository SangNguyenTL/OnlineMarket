package onlinemarket.dao;

import org.springframework.stereotype.Repository;

import onlinemarket.model.Menu;

@Repository("menuDao")
public abstract class MenuDaoImpl extends AbstractDao<Integer, Menu> implements MenuDao{

}
