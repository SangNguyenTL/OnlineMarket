package OnlineMarket.dao;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.User;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.exception.CustomException;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import OnlineMarket.model.Address;
import OnlineMarket.model.Province;

@Repository("addressDao")
public class AddressDaoImpl extends AbstractDao<Integer, Address> implements AddressDao{

	@Override
	public Address getByProvince(Province province) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("province", province));
		return (Address) criteria.uniqueResult();
	}

	@Override
	public Address getByKeyAndUser(Integer key, User user) throws CustomException {
		if(user == null) throw  new CustomException("User not found");
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("user", user));
		criteria.add(Restrictions.eq("id", key));
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
