package OnlineMarket.converter;

import OnlineMarket.model.Address;
import OnlineMarket.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.util.Locale;


@Component
public class AddressFormatter implements Formatter<Address> {

    @Autowired
    AddressService addressService;

    @Override
    public Address parse(String s, Locale locale) {
        try{
            return addressService.getByKey(Integer.parseInt(s));
        }catch (NumberFormatException e){
            return null;
        }
    }

    @Override
    public String print(Address address, Locale locale) {
        if(address.getId() == null) return null;
        return Integer.toString(address.getId());
    }
}
