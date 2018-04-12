package onlinemarket.dao.config;

import onlinemarket.form.config.MenuPositionConfig;
import org.springframework.stereotype.Repository;

@Repository("menuPositionConfigDao")
public class MenuPositionConfigDaoImpl extends AbstractCustomConfigurationDao<MenuPositionConfig> implements MenuPositionConfigDao {
}
