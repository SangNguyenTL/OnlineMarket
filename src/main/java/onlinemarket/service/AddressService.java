package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Address;
import onlinemarket.model.Province;
import onlinemarket.model.User;
import onlinemarket.result.ResultObject;
import onlinemarket.util.exception.AddressNotFoundException;
import onlinemarket.util.exception.CustomException;
import onlinemarket.util.exception.address.AddressHasOrderException;

public interface AddressService extends InterfaceService<Integer, Address> {

    void save(Address address, User user) throws CustomException;

    void update(Address address, User user) throws CustomException, AddressNotFoundException;

    Address getByProvince(Province province);

    ResultObject<Address> listByUser(User user, FilterForm filterForm) throws CustomException;

    void delete(Integer id) throws AddressHasOrderException, AddressNotFoundException;
}
