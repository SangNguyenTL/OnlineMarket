package onlinemarket.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import onlinemarket.model.Address;
import onlinemarket.model.Province;

@Repository("addressDao")
public abstract class AddressDaoImpl extends AbstractDao<Integer, Address> implements AddressDao{

	@Override
	public Address getByProvince(Province province) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("province", province));
		return (Address) criteria.uniqueResult();
	}

}
