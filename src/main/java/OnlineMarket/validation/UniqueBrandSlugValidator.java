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
public class UniqueBrandSlugValidator implements ConstraintValidator<UniqueBrandSlug, Brand>{

	@Autowired
	BrandService brandService;
	
	@Override
	public void initialize(UniqueBrandSlug constraintAnnotation) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public boolean isValid(Brand brand, ConstraintValidatorContext context) {
		
		Brand brandMod = brandService.getByDeclaration("slug", brand.getSlug());
		boolean isValid = brandMod == null || (brandMod.getId().equals(brand.getId()) && StringUtils.equals(brand.getSlug(), brandMod.getBeforeSlug()));
        if ( !isValid ) {
        	context.disableDefaultConstraintViolation();
        	context
                    .buildConstraintViolationWithTemplate( "{OnlineMarket.isUniqueBrandSlug}" )
                    .addPropertyNode( "slug" ).addConstraintViolation();
        }
		return isValid;
	}

}
