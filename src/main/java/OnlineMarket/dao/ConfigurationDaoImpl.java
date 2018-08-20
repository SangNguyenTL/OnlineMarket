package OnlineMarket.dao;

import org.springframework.stereotype.Repository;

import OnlineMarket.model.Configuration;

@Repository("configurationDao")
public class ConfigurationDaoImpl extends AbstractDao<Integer, Configuration> implements ConfigurationDao {


}
