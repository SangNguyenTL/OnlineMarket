package onlinemarket.dao.config;

public interface InterafaceCustomConfigDao<T> {
	
	public T getConfiguration(T clazz);
	
	public void saveConfiguration(T object);
	
}
