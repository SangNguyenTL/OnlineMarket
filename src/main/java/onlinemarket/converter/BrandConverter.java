package onlinemarket.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import onlinemarket.model.Brand;
import onlinemarket.service.BrandService;

@Component
public class BrandConverter implements Converter<Object, Brand> {

	@Autowired
	BrandService service;

	@Override
	public Brand convert(Object source) {
		try {
			if (source instanceof Brand)
				return (Brand) source;
			Integer id = Integer.parseInt(String.valueOf(source));
			return service.getByKey(id);
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
