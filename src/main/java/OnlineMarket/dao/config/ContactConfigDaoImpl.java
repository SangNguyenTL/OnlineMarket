package OnlineMarket.dao.config;

import org.springframework.stereotype.Repository;

import OnlineMarket.form.config.ContactConfig;

@Repository("contactConfigDao")
public class ContactConfigDaoImpl extends AbstractCustomConfigurationDao<ContactConfig> implements ContactConfigDao{
	
}
