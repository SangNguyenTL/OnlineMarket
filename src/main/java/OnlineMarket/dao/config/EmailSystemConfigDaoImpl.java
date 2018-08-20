package OnlineMarket.dao.config;

import org.springframework.stereotype.Repository;

import OnlineMarket.form.config.EmailSystemConfig;

@Repository("emailSystemConfigDao")
public class EmailSystemConfigDaoImpl extends AbstractCustomConfigurationDao<EmailSystemConfig> implements EmailSystemConfigDao {

}
