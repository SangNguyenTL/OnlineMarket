package onlinemarket.dao;

import java.io.Serializable;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDao<PK extends Serializable, T> {
    
    private final Class<T> persistentClass;
     
    @SuppressWarnings("unchecked")
    public AbstractDao(){
        this.persistentClass =(Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }
     
    @Autowired
    private SessionFactory sessionFactory;
 
    protected Session getSession(){
        return sessionFactory.getCurrentSession();
    }
 
    @SuppressWarnings("unchecked")
    public T getByKey(PK key) {
        return (T) getSession().get(persistentClass, key);
    }
    
    @SuppressWarnings("unchecked")
	public T getByDeclaration(String key, String value) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq(key, value));
		return (T) criteria.uniqueResult();
	}
 
    public void persist(T entity) {
        getSession().persist(entity);
    }
    
    public Serializable save(T entity) {
        return getSession().save(entity);
    }
    
    public void update(T entity) {
        getSession().update(entity);
    }
    
    public void delete(T entity) {
        getSession().delete(entity);
    }
 
    @SuppressWarnings("unchecked")
	public List<T> list(){
		Criteria criteria = createEntityCriteria();
		return (List<T>) criteria.list();
    }
    
    @SuppressWarnings("unchecked")
	public List<T> listByInList(String key, List<String> listValue){
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.in(key, listValue));
		return (List<T>) criteria.list();
    }
    
    protected Criteria createEntityCriteria(){
        return getSession().createCriteria(persistentClass);
    }
    
	@SuppressWarnings("unchecked")
	public List<T> list(Integer offset, Integer maxResults) {
		return (List<T>) createEntityCriteria().setFirstResult(offset != null ? offset : 0)
				.setMaxResults(maxResults != null ? maxResults : 10).list();
	}
}