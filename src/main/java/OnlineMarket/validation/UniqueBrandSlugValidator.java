package OnlineMarket.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import OnlineMarket.model.Brand;
import OnlineMarket.service.BrandService;

@Component
public class UniqueBrandSlugValidator implements ConstraintValidator<UniqueBrandSlug, Object>{

	@Autowired
	BrandService brandService;
	
	@Override
	public void initialize(UniqueBrandSlug constraintAnnotation) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public boolean isValid(Object candidate, ConstraintValidatorContext context) {
		
		Brand brand = (Brand) candidate;
		Brand brandMod = brandService.getByDeclaration("slug", brand.getSlug());
		boolean isValid = false;
		if(brandMod == null) isValid = true;
		else if(StringUtils.equals(brand.getSlug(), brand.getBeforeSlug())) isValid = true;
		else isValid = false;
        if ( !isValid ) {
        	context.disableDefaultConstraintViolation();
        	context
                    .buildConstraintViolationWithTemplate( "{OnlineMarket.isUniqueBrandSlug}" )
                    .addPropertyNode( "slug" ).addConstraintViolation();
        }
		return isValid;
	}

}
