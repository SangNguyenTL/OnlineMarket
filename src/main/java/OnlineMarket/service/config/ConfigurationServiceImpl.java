package OnlineMarket.service.config;

import java.util.List;

import OnlineMarket.dao.config.*;
import OnlineMarket.form.config.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import OnlineMarket.dao.ConfigurationDao;
import OnlineMarket.model.Configuration;


@Service("configurationService")
@Transactional
public class ConfigurationServiceImpl implements ConfigurationService {
	
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
	
	@Autowired
	private ApiConfigDao apiConfigDao;

	@Autowired
	private MenuPositionConfigDao menuPositionConfigDao;

	@Autowired
	private Environment environment;
	
	@Override
	public Configuration getByKey(Integer id) {
		return dao.getByKey(id);
	}
	
	@Override
	public Configuration getByDeclaration(String key, Object value) {
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
        dao.merge(conf);
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
	public ContactConfig getContact() {
		return contactConfigDao.getConfiguration(new ContactConfig());
	}

	@Override
	public EmailSystemConfig getEmail() {
		Integer port;
		try {
			port = Integer.parseInt(environment.getProperty("mail.port"));
		}catch (NumberFormatException ex){
			port = 578;
		}
		EmailSystemConfig emailSystemConfig = new EmailSystemConfig(environment.getProperty("mail.host"), port, environment.getProperty("mail.userName"), environment.getProperty("mail.password"));
		return emailSystemConfigDao.getConfiguration(emailSystemConfig);
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
	public ApiConfig getApiConfig() {
		return apiConfigDao.getConfiguration(new ApiConfig());
	}

	@Override
	public MenuPositionConfig getMenuPositionConfig() {
		return menuPositionConfigDao.getConfiguration(new MenuPositionConfig());
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
	
	@Override
	public void saveApiConfig(ApiConfig apiConfig) {
		apiConfigDao.saveConfiguration(apiConfig);
	}

	@Override
	public void saveMenuPositionConfig(MenuPositionConfig menuPositionConfig) {
		menuPositionConfigDao.saveConfiguration(menuPositionConfig);
	}
}
