package onlinemarket.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import onlinemarket.model.ProductCategory;
import onlinemarket.service.ProductCategoryService;

@Component
public class UniqueProductSlugValidator implements ConstraintValidator<UniqueProductCategorySlug, Object>{

	@Autowired
	ProductCategoryService productCategoryService;
	
	@Override
	public void initialize(UniqueProductCategorySlug constraintAnnotation) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public boolean isValid(Object candidate, ConstraintValidatorContext context) {
		
		ProductCategory productCategory = (ProductCategory) candidate;
		ProductCategory productCategoryMod = productCategoryService.getByDeclaration("slug", productCategory.getSlug());
		boolean isValid = false;
		if(productCategoryMod == null || StringUtils.equals(productCategory.getSlug(), productCategory.getBeforeSlug())) isValid = true;
		else isValid = false;
        if ( !isValid ) {
        	context.disableDefaultConstraintViolation();
        	context
                    .buildConstraintViolationWithTemplate( "{onlinemarket.isUniqueProductCategorySlug}" )
                    .addPropertyNode( "slug" ).addConstraintViolation();
        }
		return isValid;
	}

}
