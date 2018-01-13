package onlinemarket.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import onlinemarket.model.ProductCategory;
import onlinemarket.service.ProductCategoryService;

@Component
public class ProductCategoryConverter implements Converter<Object, ProductCategory> {

	@Autowired
	ProductCategoryService productCategoryService;

	@Override
	public ProductCategory convert(Object source) {
		try {
			if (source instanceof ProductCategory)
				return (ProductCategory) source;
			Integer id = Integer.parseInt(String.valueOf(source));
			return productCategoryService.getByKey(id);
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
