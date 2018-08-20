package OnlineMarket.dao;


import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Address;
import OnlineMarket.model.Province;
import OnlineMarket.model.User;
import OnlineMarket.result.ResultObject;

public interface AddressDao extends InterfaceDao<Integer ,Address>{

	Address getByProvince(Province province);

	ResultObject<Address> listByUser(User user, FilterForm filterForm);

}
