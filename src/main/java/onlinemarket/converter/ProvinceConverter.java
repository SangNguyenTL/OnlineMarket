package onlinemarket.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import onlinemarket.model.Province;
import onlinemarket.service.ProvinceService;

@Component
public class ProvinceConverter implements Converter<Object, Province>{

	@Autowired
	ProvinceService proviceService;
	
	@Override
	public Province convert(Object source) {
		try {
			Byte id = Byte.parseByte((String)source);
			return proviceService.getByKey(id);
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
