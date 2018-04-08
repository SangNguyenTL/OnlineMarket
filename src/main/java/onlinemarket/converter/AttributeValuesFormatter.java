package onlinemarket.converter;

import onlinemarket.model.AttributeValues;
import org.springframework.format.Formatter;

import java.util.Locale;

public class AttributeValuesFormatter implements Formatter<AttributeValues> {
    @Override
    public AttributeValues parse(String s, Locale locale) {
        try {
            Integer id = Integer.parseInt(String.valueOf(s));
            return new AttributeValues(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String print(AttributeValues o, Locale locale) {
        if(o.getId() == null) return null;
        return Integer.toString(o.getId());
    }
}
