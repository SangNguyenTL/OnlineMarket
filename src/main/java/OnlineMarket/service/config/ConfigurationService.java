package OnlineMarket.service.config;


import OnlineMarket.form.config.*;
import OnlineMarket.model.Configuration;
import OnlineMarket.service.InterfaceService;


public interface ConfigurationService extends InterfaceService<Integer, Configuration>{
	
	boolean isConfigurationUnique(String key);
	
	ContactConfig getContact();
	
	EmailSystemConfig getEmail();
	
	LogoConfig getLogo();
	
	SocialConfig getSocial();
	
	UploadConfig getUpload();
	
	GeneralConfig getGeneral();
	
	ApiConfig getApiConfig();

	MenuPositionConfig getMenuPositionConfig();
	
	void saveGeneralConfig(GeneralConfig generalConfig);
	
	void saveContactConfig(ContactConfig contactConfig);
	
	void saveEmailSystemConfig(EmailSystemConfig emailSystemConfig);
	
	void saveLogoConfig(LogoConfig logoConfig);
	
	void saveSocialConfig(SocialConfig socialConfig);
	
	void saveUploadconfig(UploadConfig uploadConfig);
	
	void saveApiConfig(ApiConfig apiConfig);

	void saveMenuPositionConfig(MenuPositionConfig menuPositionConfig);
}
