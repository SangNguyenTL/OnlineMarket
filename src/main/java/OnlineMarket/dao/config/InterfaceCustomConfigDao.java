package OnlineMarket.dao.config;

public interface InterfaceCustomConfigDao<T> {
	
	T getConfiguration(T clazz);

	void saveConfiguration(T object);
	
}
