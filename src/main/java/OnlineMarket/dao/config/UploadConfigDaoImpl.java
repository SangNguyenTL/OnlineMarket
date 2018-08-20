package OnlineMarket.dao.config;

import org.springframework.stereotype.Repository;

import OnlineMarket.form.config.UploadConfig;

@Repository("uploadConfigDao")
public class UploadConfigDaoImpl extends AbstractCustomConfigurationDao<UploadConfig> implements UploadConfigDao{

}
