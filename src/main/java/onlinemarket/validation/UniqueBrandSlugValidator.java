package onlinemarket.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import onlinemarket.model.Brand;
import onlinemarket.service.BrandService;

@Component
public class UniqueBrandSlugValidator implements ConstraintValidator<UniqueBrandSlug, String>{

	@Autowired
	BrandService brandService;
	
	@Override
	public void initialize(UniqueBrandSlug constraintAnnotation) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Brand brand = brandService.getByDeclaration("slug", value);
		return brand == null;
	}

}
