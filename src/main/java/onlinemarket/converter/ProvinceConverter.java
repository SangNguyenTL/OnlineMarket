package onlinemarket.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import onlinemarket.model.Province;
import onlinemarket.service.ProvinceService;

@Component
public class ProvinceConverter implements Converter<Object, Province>{

	@Autowired
	ProvinceService provinceService;
	
	@Override
	public Province convert(Object source) {
		try {
			if(source instanceof Province) return (Province) source;
			Integer id = Integer.parseInt(String.valueOf(source));
			return provinceService.getByKey(id);
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
