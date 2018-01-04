package onlinemarket.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import onlinemarket.model.Role;
import onlinemarket.service.RoleService;

@Component
public class RoleConverter implements Converter<Object, Role>{

	@Autowired
	RoleService roleService;
	
	@Override
	public Role convert(Object source) {
		Integer id = Integer.parseInt((String)source);
		return roleService.getByKey(id);
	}
	

}
