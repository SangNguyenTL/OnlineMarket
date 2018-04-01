package onlinemarket.dao;


import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Address;
import onlinemarket.model.Province;
import onlinemarket.model.User;
import onlinemarket.result.ResultObject;

public interface AddressDao extends InterfaceDao<Integer ,Address>{

	Address getByProvince(Province province);

	ResultObject<Address> listByUser(User user, FilterForm filterForm);

}
