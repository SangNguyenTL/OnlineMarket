package onlinemarket.dao.config;

import org.springframework.stereotype.Repository;

import onlinemarket.form.config.ApiConfig;

@Repository("apiConfigDao")
public class ApiConfigDaoImpl  extends AbstractCustomConfigurationDao<ApiConfig> implements ApiConfigDao{

}
