package onlinemarket.dao;

import org.springframework.stereotype.Repository;

import onlinemarket.model.Configuration;

@Repository("configurationDao")
public class ConfigurationDaoImpl extends AbstractDao<Integer, Configuration> implements ConfigurationDao {


}
