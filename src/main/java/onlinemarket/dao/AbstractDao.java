package onlinemarket.dao;

import java.io.Serializable;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.Map.Entry;

import onlinemarket.util.Help;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.result.ResultObject;

public abstract class AbstractDao<PK extends Serializable, T> {

    private final Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public AbstractDao() {
        this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    private void addAll(Criteria criteria, Map<String, String> map){
        for (Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey(),
                    value = entry.getValue();
            String[] keyArr = key.split("\\.");
            if (keyArr.length == 2) {
                key = keyArr[0] + "Alias";
                criteria.createAlias(keyArr[0], key);
                key = key + "." + keyArr[1];
            }
            if(StringUtils.isNotBlank(value))
                if (Help.isInteger(value) && key.equals("state"))
                    criteria.add(Restrictions.eq(key, Byte.parseByte(value)));
                else if(Help.isInteger(value))  criteria.add(Restrictions.eq(key, Integer.parseInt(value)));
                else
                    criteria.add(Restrictions.eq(key, value));
        }
    }

    @SuppressWarnings("unchecked")
    public T getByKey(PK key) {
        return (T) getSession().get(persistentClass, key);
    }

    @SuppressWarnings("unchecked")
    public T getByDeclaration(String key, Object value) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq(key, value));
        List<T> list =  criteria.list();
        if(list.size() > 0) {
            return  list.get(0);
        }else
            return null;
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

    public Object merge(T entity) {
       return getSession().merge(entity);
    }

    public void delete(T entity) {
        getSession().delete(entity);
    }

    @SuppressWarnings("unchecked")
    public List<T> list() {
        Criteria criteria = createEntityCriteria();
        return (List<T>) criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<T> listByDeclaration(String propertyName, Object object) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq(propertyName, object));
        return (List<T>) criteria.list();
    }

    public long count() {
        Criteria criteria = createEntityCriteria();
        criteria.setProjection(Projections.rowCount());
        return (long) criteria.uniqueResult();
    }

    public long countBy(String key, Object value) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq(key, value));
        criteria.setProjection(Projections.rowCount());
        return (long) criteria.uniqueResult();
    }

    public T getUniqueResultBy(String key, Object value) {
        Criteria criteria = createEntityCriteria();

        String[] arrayKey = key.split("\\.");
        if(arrayKey.length == 2){
            key = arrayKey[0] + "Alias";
            criteria.createAlias(arrayKey[0], key);
            key = key + "." + arrayKey[1];
        }
        criteria.add(Restrictions.eq(key, value));
        List<T> list =  criteria.list();
        if(list.size() > 0) {
            return  list.get(0);
        }else
            return null;
    }

    @SuppressWarnings("unchecked")
    public List<T> listByInList(String key, List<String> listValue) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.in(key, listValue));
        return (List<T>) criteria.list();
    }

    @SuppressWarnings("unchecked")
    public ResultObject<T> listByInList(String key, List<Object> listValue, FilterForm filterForm) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.in(key, listValue));
        return childFilterFrom(criteria, filterForm);
    }

    protected Criteria createEntityCriteria() {
        return getSession().createCriteria(persistentClass);
    }

    @SuppressWarnings("unchecked")
    public List<T> list(Integer offset, Integer maxResults) {
        return (List<T>) createEntityCriteria().setFirstResult(offset != null ? offset : 0)
                .setMaxResults(maxResults != null ? maxResults : 10).list();
    }

    @SuppressWarnings("unchecked")
    public ResultObject<T> list(FilterForm filterForm) {

        Criteria criteria = createEntityCriteria();
        return childFilterFrom(criteria, filterForm);
    }

    public ResultObject<T> listByDeclaration(String propertyName, Object object, FilterForm filterForm){

        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq(propertyName, object));
        return childFilterFrom(criteria, filterForm);

    }

    ResultObject<T> childFilterFrom(Criteria criteria, FilterForm filterForm) {

        ResultObject<T> result = new ResultObject<>();

        if (StringUtils.isNotBlank(filterForm.getSearch()) && StringUtils.isNotBlank(filterForm.getSearchBy())) {
            String key = filterForm.getSearchBy();
            String[] keyArr = key.split("\\.");
            if (keyArr.length == 2) {
                key = keyArr[0] + "Alias";
                criteria.createAlias(keyArr[0], key);
                key = key + "." + keyArr[1];
            }
            criteria.add(Restrictions.ilike(key, "%" + filterForm.getSearch() + "%"));
        }

        addAll(criteria, filterForm.getGroupSearch());

//        map(criteria, filterForm.getWhere());

        criteria.setProjection(Projections.rowCount());
        Long count = (Long) criteria.uniqueResult();
        if (count != null) {
            int totalPages = (int) Math.ceil((float) count / filterForm.getSize());
            result.setTotalPages(totalPages);
        } else result.setTotalPages(0);

        if (filterForm.getSize() > 0 && filterForm.getCurrentPage() > 0)
            criteria.setFirstResult((filterForm.getCurrentPage() - 1) * filterForm.getSize()).setMaxResults(filterForm.getSize());
        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        if (StringUtils.isNotBlank(filterForm.getOrderBy()) && StringUtils.isNotBlank(filterForm.getOrder())) {

            String key = filterForm.getOrderBy();
            String[] keyArr = key.split("\\.");
            if (keyArr.length == 2) {
                key = keyArr[0] + "Alias";
                criteria.createAlias(keyArr[0], key);
                key = key + "." + keyArr[1];
            }

            if (filterForm.getOrder().equals("asc"))
                criteria.addOrder(Order.asc(key));
            else
                criteria.addOrder(Order.desc(key));
        }

        result.setList(criteria.list());
        if(result.getList() == null)
            result.setList(new ArrayList<T>());
        result.setCurrentPage(filterForm.getCurrentPage());

        return result;
    }
}