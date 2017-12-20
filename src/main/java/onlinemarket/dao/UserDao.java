package onlinemarket.dao;

import onlinemarket.model.User;

public interface UserDao {
    User findById(int id);
    
    User findBySSO(String sso);
}
