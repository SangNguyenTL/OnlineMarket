package onlinemarket.dao.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import onlinemarket.dao.ConfigurationDao;
import onlinemarket.model.Configuration;

public abstract class AbstractCustomConfigurationDao<T> {
	
	@Autowired
	private ConfigurationDao configurationDao;

	public T getConfiguration(T clazz) {
		ArrayList<String> listValue = new ArrayList<>();
		for (Field field : clazz.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			listValue.add(field.getName());
		}
    	List<Configuration> listConf = this.configurationDao.listByInList("key", listValue);
    	
    	for (Configuration configuration : listConf) {
    		try {
        		Field fieldObject = clazz.getClass().getDeclaredField(configuration.getKey());
        		fieldObject.setAccessible(true);
        		String val = configuration.getValue();
        		if(fieldObject.getClass().equals(Integer.class)) {
        			try {
        				fieldObject.set(clazz, Integer.parseInt(val));
					} catch (NumberFormatException e) {
						
					}
        		}else fieldObject.set(clazz, val);
				
			} catch (NoSuchFieldException | NullPointerException | IllegalAccessException e) {
				e.printStackTrace();
				continue;
			}
		}
		return clazz;
    }
    
    public void saveConfiguration(T clazz) {
    	for(Field field : clazz.getClass().getDeclaredFields()) {
    		try {
    			field.setAccessible(true);
    			String nameField = field.getName();
        		String valueField = (String) field.get(clazz);
        		Configuration confDb = this.configurationDao.getByDeclaration("key", nameField);
        		if(confDb == null) {
        			confDb = new Configuration(nameField, valueField);
        			this.configurationDao.persist(confDb);
        		}else {
        			confDb.setValue(valueField);
        			this.configurationDao.update(confDb);
        		}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
    	}
    }
	
}
