package OnlineMarket.dao.config;

import org.springframework.stereotype.Repository;

import OnlineMarket.form.config.ApiConfig;

@Repository("apiConfigDao")
public class ApiConfigDaoImpl  extends AbstractCustomConfigurationDao<ApiConfig> implements ApiConfigDao{

}
