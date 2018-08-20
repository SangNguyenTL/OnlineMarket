package OnlineMarket.dao;

import org.springframework.stereotype.Repository;

import OnlineMarket.model.MenuGroup;

@Repository("menuGroupDao")
public class MenuGroupDaoImpl extends AbstractDao<Integer, MenuGroup> implements MenuGroupDao{

}
