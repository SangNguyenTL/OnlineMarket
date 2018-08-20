package OnlineMarket.dao.config;

import org.springframework.stereotype.Repository;

import OnlineMarket.form.config.GeneralConfig;

@Repository("generalConfigDao")
public class GeneralConfigDaoImpl extends AbstractCustomConfigurationDao<GeneralConfig> implements GeneralConfigDao{

}
