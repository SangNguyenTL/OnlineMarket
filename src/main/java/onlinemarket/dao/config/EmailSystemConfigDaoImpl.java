package onlinemarket.dao.config;

import org.springframework.stereotype.Repository;

import onlinemarket.form.config.EmailSystemConfig;

@Repository("emailSystemConfigDao")
public class EmailSystemConfigDaoImpl extends AbstractCustomConfigurationDao<EmailSystemConfig> implements EmailSystemConfigDao {

}
