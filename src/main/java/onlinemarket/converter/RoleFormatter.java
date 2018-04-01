package onlinemarket.converter;

import onlinemarket.model.Role;
import org.springframework.format.Formatter;

import java.util.Locale;

public class RoleFormatter implements Formatter<Role> {
    @Override
    public Role parse(String s, Locale locale) {
        try {
            Integer id = Integer.parseInt(String.valueOf(s));
            return new Role(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String print(Role o, Locale locale) {
        return Integer.toString(o.getId());
    }
}
