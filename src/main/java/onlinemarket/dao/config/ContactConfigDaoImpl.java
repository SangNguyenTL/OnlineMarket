package onlinemarket.dao.config;

import org.springframework.stereotype.Repository;

import onlinemarket.form.config.ContactConfig;

@Repository("contactConfigDao")
public class ContactConfigDaoImpl extends AbstractCustomConfigurationDao<ContactConfig> implements ContactConfigDao{
	
}
