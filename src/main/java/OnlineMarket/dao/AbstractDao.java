package OnlineMarket.dao;

import java.io.Serializable;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.result.ResultObject;

public abstract class AbstractDao<PK extends Serializable, T> {

    private final Class<T> persistentClass;

    private Pattern patternOrder;

    private Matcher matcher;

    @SuppressWarnings("unchecked")
    public AbstractDao() {
        this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        patternOrder = Pattern.compile("(\\w+)(\\.\\w+)?");
    }

    @Autowired
    private SessionFactory sessionFactory;

    Session getSession() {
        return sessionFactory.getCurrentSession();
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

    @SuppressWarnings("unchecked")
    public T merge(T entity) {
       return (T) getSession().merge(entity);
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

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    ResultObject<T> childFilterFrom(Criteria criteria, FilterForm filterForm) {

        ResultObject<T> result = new ResultObject<>();

        if (StringUtils.isNotBlank(filterForm.getSearch()) && StringUtils.isNotBlank(filterForm.getSearchBy())) {
            matcher = patternOrder.matcher(filterForm.getSearchBy());
            if(matcher.matches()){
                String key = matcher.group(1);
                try {
                    Field field = persistentClass.getDeclaredField(key);
                    if(StringUtils.isNotBlank(key)){
                        if(matcher.group(2) != null){
                            switch (field.getType().getSimpleName()){
                                case "List":
                                case "Set":
                                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                                    Class<?> aClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                                    aClass.getDeclaredField(matcher.group(2).substring(1));
                                    break;
                            }
                                key = key + "Alias";
                            criteria.createAlias(matcher.group(1), key);
                            key = key+matcher.group(2);
                        }
                        criteria.add(Restrictions.ilike(key, "%" + filterForm.getSearch() + "%"));
                    }
                } catch (NoSuchFieldException ignore) {
                }
            }
        }

        addAll(criteria, filterForm.getGroupSearch());

        criteria.setProjection(Projections.rowCount());
        Long count = (Long) criteria.uniqueResult();
        if (filterForm.getSize()!= null && count != null) {
            int totalPages = (int) Math.ceil((float) count / filterForm.getSize());
            result.setTotalPages(totalPages);
            result.setCount(count);

            if (filterForm.getSize() > 0 && filterForm.getCurrentPage() > 0)
                criteria.setFirstResult((filterForm.getCurrentPage() - 1) * filterForm.getSize()).setMaxResults(filterForm.getSize());

        } else if(filterForm.getSize() == null && count != null) result.setTotalPages(1);
        else result.setTotalPages(0);


        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        if(StringUtils.isNotBlank(filterForm.getOrderBy()) && StringUtils.isNotBlank(filterForm.getOrder())){

            matcher = patternOrder.matcher(filterForm.getOrderBy());
            if(matcher.matches()){
                String key = matcher.group(1);
                try{
                    Field field = persistentClass.getDeclaredField(key);
                    Field childField = null;
                    if(StringUtils.isNotBlank(key)){
                        if(matcher.group(2) != null){
                            switch (field.getType().getSimpleName()){
                                case "List":
                                case "Set":
                                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                                    Class<?> aClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                                    childField = aClass.getDeclaredField(matcher.group(2).substring(1));
                                    break;
                            }
                            key = key + "Alias";
                            criteria.createAlias(matcher.group(1), key);
                            if(childField!=null){
                                if(field.getName().equals("productViewsSet") && childField.getName().equals("count")){
                                    Calendar cal = Calendar.getInstance();
                                    cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
                                    cal.clear(Calendar.MINUTE);
                                    cal.clear(Calendar.SECOND);
                                    cal.clear(Calendar.MILLISECOND);

                                    cal.add(Calendar.DATE, +1);
                                    criteria.add(Restrictions.le(key+".datetime", cal.getTime()));
                                    cal.add(Calendar.DATE, -1);
                                    criteria.add(Restrictions.ge(key+".datetime", cal.getTime()));
                                }
                            }
                            key = key+matcher.group(2);
                        }

                        if (filterForm.getOrder().equals("asc") || !filterForm.getOrder().equals("desc"))
                            criteria.addOrder(Order.asc(key));
                        else
                            criteria.addOrder(Order.desc(key));
                    }
                }catch (NoSuchFieldException ignore) {
                }
            }
        }

        result.setList(criteria.list());
        if(result.getList() == null)
            result.setList(new ArrayList<T>());
        result.setCurrentPage(filterForm.getCurrentPage());

        return result;
    }

    private void addAll(Criteria criteria, Map<String, String> map){
        for (Entry<String, String> entry : map.entrySet()) {
            matcher = patternOrder.matcher(entry.getKey());
            if(matcher.matches()){
                String key = matcher.group(1), value = entry.getValue();
                try {
                    Field field = persistentClass.getDeclaredField(key);
                    String nameTypeValue = field.getType().getSimpleName();
                    if(StringUtils.isNotBlank(key)){
                        if(matcher.group(2) != null){
                            switch (nameTypeValue){
                                case "List":
                                case "Set":
                                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                                    Class<?> aClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                                    aClass.getDeclaredField(matcher.group(2).substring(1));
                                    break;
                                case "int":
                                case "Integer":
                                case "Long":
                                case "String":
                                case "long":
                                case "byte":
                                case "Byte":
                                    break;
                                default:
                                    nameTypeValue = field.getType().getDeclaredField(matcher.group(2).substring(1)).getType().getSimpleName();
                            }
                            key = key + "Alias";
                            criteria.createAlias(matcher.group(1), key);
                            key = key+matcher.group(2);
                        }

                        if(StringUtils.isNotBlank(value)){
                            switch (nameTypeValue){
                                case "byte":
                                case "Byte":
                                    criteria.add(Restrictions.eq(key, Byte.parseByte(value)));
                                    break;
                                case "Integer":
                                case "int":
                                    criteria.add(Restrictions.eq(key, Integer.parseInt(value)));
                                    break;
                                case "long":
                                case "Long":
                                    criteria.add(Restrictions.eq(key, Long.parseLong(value)));
                                    break;
                                case "String":
                                    criteria.add(Restrictions.eq(key, value));
                                    break;
                            }
                        }
                    }
                } catch (NumberFormatException|NoSuchFieldException ignore) {

                }
            }
        }
    }
}