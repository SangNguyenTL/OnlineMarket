package onlinemarket.dao.config;

import org.springframework.stereotype.Repository;

import onlinemarket.form.config.UploadConfig;

@Repository("uploadConfigDao")
public class UploadConfigDaoImpl extends AbstractCustomConfigurationDao<UploadConfig> implements UploadConfigDao{

}
