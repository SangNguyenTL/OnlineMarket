package onlinemarket.dao;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.User;
import onlinemarket.result.ResultObject;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import onlinemarket.model.Address;
import onlinemarket.model.Province;

@Repository("addressDao")
public class AddressDaoImpl extends AbstractDao<Integer, Address> implements AddressDao{

	@Override
	public Address getByProvince(Province province) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("province", province));
		return (Address) criteria.uniqueResult();
	}

	@Override
	public ResultObject<Address> listByUser(User user, FilterForm filterForm) {

		Criteria criteria = createEntityCriteria();
		ResultObject<Address> result = new ResultObject<>();
		criteria.add(Restrictions.eq("user", user));

		return childFilterFrom(criteria, filterForm);
	}

}
