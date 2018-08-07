package onlinemarket.dao;

import java.io.Serializable;
import java.util.List;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.result.ResultObject;


interface InterfaceDao<PK extends Serializable,T> {
	
	T getByKey(PK key);
	
	T getByDeclaration(String key, Object value);

	T getUniqueResultBy(String key, Object value);
	
	List<T> list();
	
	List<T> listByDeclaration(String propertyName, Object object);

	ResultObject<T> listByDeclaration(String propertyName, Object object, FilterForm filterForm);
	
	List<T> list(Integer offset, Integer maxResults);
	
	List<T> listByInList(String key, List<String> listValue);

	ResultObject<T> listByInList(String key, List<Object> listValue, FilterForm filterForm);
	
	void delete(T entity);
	
	void persist(T entity);

	Serializable save(T entity);

	Object merge(T entity);

	void update(T entity);
	
	long count();

	long countBy(String key, Object value);

	ResultObject<T> list(FilterForm filterForm);
}
