package onlinemarket.service.config;


import onlinemarket.form.config.ApiConfig;
import onlinemarket.form.config.ContactConfig;
import onlinemarket.form.config.EmailSystemConfig;
import onlinemarket.form.config.GeneralConfig;
import onlinemarket.form.config.LogoConfig;
import onlinemarket.form.config.SocialConfig;
import onlinemarket.form.config.UploadConfig;
import onlinemarket.model.Configuration;
import onlinemarket.service.InterfaceService;


public interface ConfigurationService extends InterfaceService<Integer, Configuration>{
	
	boolean isConfigurationUnique(String key);
	
	ContactConfig getContag();
	
	EmailSystemConfig getEmail();
	
	LogoConfig getLogo();
	
	SocialConfig getSocial();
	
	UploadConfig getUpload();
	
	GeneralConfig getGeneral();
	
	ApiConfig getApiConfig();
	
	void saveGeneralConfig(GeneralConfig generalConfig);
	
	void saveContactConfig(ContactConfig contactConfig);
	
	void saveEmailSystemConfig(EmailSystemConfig emailSystemConfig);
	
	void saveLogoConfig(LogoConfig logoConfig);
	
	void saveSocialConfig(SocialConfig socialConfig);
	
	void saveUploadconfig(UploadConfig uploadConfig);
	
	void saveApiConfig(ApiConfig apiConfig);
}
