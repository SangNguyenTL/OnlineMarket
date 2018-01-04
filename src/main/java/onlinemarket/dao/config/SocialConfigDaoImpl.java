package onlinemarket.dao.config;

import org.springframework.stereotype.Repository;

import onlinemarket.form.config.SocialConfig;

@Repository("socialConfigDao")
public class SocialConfigDaoImpl extends AbstractCustomConfigurationDao<SocialConfig> implements SocialConfigDao{
	
}
