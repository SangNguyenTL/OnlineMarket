package onlinemarket.service.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.ConfigurationDao;
import onlinemarket.dao.config.ContactConfigDao;
import onlinemarket.dao.config.EmailSystemConfigDao;
import onlinemarket.dao.config.GeneralConfigDao;
import onlinemarket.dao.config.LogoConfigDao;
import onlinemarket.dao.config.SocialConfigDao;
import onlinemarket.dao.config.UploadConfigDao;
import onlinemarket.form.config.ContactConfig;
import onlinemarket.form.config.EmailSystemConfig;
import onlinemarket.form.config.GeneralConfig;
import onlinemarket.form.config.LogoConfig;
import onlinemarket.form.config.SocialConfig;
import onlinemarket.form.config.UploadConfig;
import onlinemarket.model.Configuration;


@Service("configurationService")
@Transactional
public class ConfigurationServiceImp implements ConfigurationService {
	
	@Autowired
	private ConfigurationDao dao;
	
	@Autowired
	private ContactConfigDao contactConfigDao;
	
	@Autowired
	private EmailSystemConfigDao emailSystemConfigDao;
	
	@Autowired
	private GeneralConfigDao generalConfigDao;
	
	@Autowired
	private LogoConfigDao logoconfigDao;
	
	@Autowired
	private SocialConfigDao socialConfigDao;
	
	@Autowired
	private UploadConfigDao uploadConfigDao;
	
	@Override
	public Configuration getByKey(Integer id) {
		return dao.getByKey(id);
	}
	
	@Override
	public Configuration getByDeclaration(String key, String value) {
		return dao.getByDeclaration(key, value);
	}
	
	@Override
	public List<Configuration> list() {
		return dao.list();
	}

	@Override
	public void delete(Configuration configuration) {
		dao.delete(configuration);
	}

	@Override
	public void save(Configuration conf) {
		dao.persist(conf);
	}
	
	@Override
	public void update(Configuration conf) {
        dao.update(conf);
	}

	@Override
	public List<Configuration> list(Integer offset, Integer maxResults) {
		return null;
	}
	
	@Override
    public boolean isConfigurationUnique(String key) {
        Configuration configuration = getByDeclaration("key", key);
        return configuration == null;
    }

	@Override
	public ContactConfig getContag() {
		return contactConfigDao.getConfiguration(new ContactConfig());
	}

	@Override
	public EmailSystemConfig getEmail() {
		return emailSystemConfigDao.getConfiguration(new EmailSystemConfig());
	}

	@Override
	public LogoConfig getLogo() {
		return logoconfigDao.getConfiguration(new LogoConfig());
	}

	@Override
	public SocialConfig getSocial() {
		return socialConfigDao.getConfiguration(new SocialConfig());
	}

	@Override
	public UploadConfig getUpload() {
		return uploadConfigDao.getConfiguration(new UploadConfig());
	}

	@Override
	public GeneralConfig getGeneral() {
		return generalConfigDao.getConfiguration(new GeneralConfig());
	}

	@Override
	public void saveGeneralConfig(GeneralConfig generalConfig) {
		generalConfigDao.saveConfiguration(generalConfig);
	}

	@Override
	public void saveContactConfig(ContactConfig contactConfig) {
		contactConfigDao.saveConfiguration(contactConfig);
	}

	@Override
	public void saveEmailSystemConfig(EmailSystemConfig emailSystemConfig) {
		 emailSystemConfigDao.saveConfiguration(emailSystemConfig);
	}

	@Override
	public void saveLogoConfig(LogoConfig logoConfig) {
		logoconfigDao.saveConfiguration(logoConfig);
	}

	@Override
	public void saveSocialConfig(SocialConfig socialConfig) {
		socialConfigDao.saveConfiguration(socialConfig);
	}

	@Override
	public void saveUploadconfig(UploadConfig uploadConfig) {
		uploadConfigDao.saveConfiguration(uploadConfig);
	}
}
