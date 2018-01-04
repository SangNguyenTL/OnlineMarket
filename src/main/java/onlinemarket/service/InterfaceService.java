package onlinemarket.service;

import java.io.Serializable;
import java.util.List;

public interface InterfaceService<PK extends Serializable,T> {
	
	void save(T entity);
	
	void update(T entity);
	
	void delete(T entity);
	
	T getByKey(PK key);
	
	T getByDeclaration(String key, String value);
	
	List<T> list();
	
	List<T> list(Integer offset, Integer maxResults);
}
