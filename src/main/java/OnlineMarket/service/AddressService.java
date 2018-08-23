package OnlineMarket.service;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Address;
import OnlineMarket.model.Province;
import OnlineMarket.model.User;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.exception.AddressNotFoundException;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.exception.address.AddressHasOrderException;

import java.util.List;

public interface AddressService extends InterfaceService<Integer, Address> {

    Address getByKeyAndUser(Integer key, User user) throws CustomException;

    void save(Address address, User user) throws CustomException;

    void update(Address address, User user) throws CustomException, AddressNotFoundException;

    Address getByProvince(Province province);

    ResultObject<Address> listByUser(User user, FilterForm filterForm) throws CustomException;

    void delete(Integer id) throws AddressHasOrderException, AddressNotFoundException;

    List<Address> listByDeclaration(String value, Object object);

}
