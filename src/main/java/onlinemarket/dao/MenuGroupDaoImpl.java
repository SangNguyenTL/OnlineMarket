package onlinemarket.dao;

import org.springframework.stereotype.Repository;

import onlinemarket.model.MenuGroup;

@Repository("menuGroupDao")
public class MenuGroupDaoImpl extends AbstractDao<Integer, MenuGroup> implements MenuGroupDao{

}
