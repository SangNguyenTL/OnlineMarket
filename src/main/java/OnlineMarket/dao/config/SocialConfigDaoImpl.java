package OnlineMarket.dao.config;

import org.springframework.stereotype.Repository;

import OnlineMarket.form.config.SocialConfig;

@Repository("socialConfigDao")
public class SocialConfigDaoImpl extends AbstractCustomConfigurationDao<SocialConfig> implements SocialConfigDao{
	
}
