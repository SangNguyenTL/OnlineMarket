package onlinemarket.dao.config;

import org.springframework.stereotype.Repository;

import onlinemarket.form.config.GeneralConfig;

@Repository("generalConfigDao")
public class GeneralConfigDaoImpl extends AbstractCustomConfigurationDao<GeneralConfig> implements GeneralConfigDao{

}
