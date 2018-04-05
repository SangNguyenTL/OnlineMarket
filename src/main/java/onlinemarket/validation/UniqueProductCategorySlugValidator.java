package onlinemarket.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import onlinemarket.model.ProductCategory;
import onlinemarket.service.ProductCategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;


@Component
public class UniqueProductCategorySlugValidator implements ConstraintValidator<UniqueProductCategorySlug, Object>{

	@Autowired
	ProductCategoryService productCategoryService;
	
	@Override
	public void initialize(UniqueProductCategorySlug constraintAnnotation) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public boolean isValid(Object candidate, ConstraintValidatorContext context) {
		
		ProductCategory productCategory = (ProductCategory) candidate;
		ProductCategory productCategory1 = productCategoryService.getByDeclaration("slug", productCategory.getSlug());
		boolean isValid = false;
		if(productCategory1 == null || StringUtils.equals(productCategory1.getSlug(), productCategory.getBeforeSlug())) isValid = true;
		else isValid = false;
        if ( !isValid ) {
        	context.disableDefaultConstraintViolation();
        	context
                    .buildConstraintViolationWithTemplate( "{onlinemarket.isUniqueProductSlug}" )
                    .addPropertyNode( "slug" ).addConstraintViolation();
        }
		return isValid;

	}

}
