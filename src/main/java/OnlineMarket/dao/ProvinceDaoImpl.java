package OnlineMarket.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import OnlineMarket.model.Province;

import java.util.List;

@Repository("provinceDao")
public class ProvinceDaoImpl extends AbstractDao<Integer, Province> implements ProvinceDao{

    @Override
    @SuppressWarnings("unchecked")
    public List<Province> list() {
        Criteria criteria = createEntityCriteria();
        criteria.addOrder(Order.asc("name"));
        return (List<Province>) criteria.list();
    }
}
