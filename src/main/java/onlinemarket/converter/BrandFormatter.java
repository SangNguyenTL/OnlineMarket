package onlinemarket.converter;

import onlinemarket.model.Brand;
import onlinemarket.model.Role;
import org.springframework.format.Formatter;

import java.util.Locale;

public class BrandFormatter implements Formatter<Brand> {
    @Override
    public Brand parse(String s, Locale locale) {
        try {
            Integer id = Integer.parseInt(String.valueOf(s));
            return new Brand(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String print(Brand o, Locale locale) {
        if(o.getId() == null) return null;
        return Integer.toString(o.getId());
    }
}
