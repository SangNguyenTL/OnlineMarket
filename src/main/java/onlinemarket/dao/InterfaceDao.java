package onlinemarket.dao;

import java.io.Serializable;
import java.util.List;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.result.ResultObject;


interface InterfaceDao<PK extends Serializable,T> {
	
	T getByKey(PK key);
	
	T getByDeclaration(String key, String value);
	
	List<T> list();
	
	List<T> list(Integer offset, Integer maxResults);
	
	List<T> listByInList(String key, List<String> listValue);
	
	void delete(T entity);
	
	void persist(T entity);
	
	Serializable save(T entity);
	
	void update(T entity);
	
	long count();
	
	public ResultObject<T> list(FilterForm filterForm);
}
