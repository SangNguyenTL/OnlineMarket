package OnlineMarket.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import OnlineMarket.model.ProductCategory;
import OnlineMarket.service.ProductCategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;


@Component
public class UniqueProductCategorySlugValidator implements ConstraintValidator<UniqueProductCategorySlug, ProductCategory>{

	@Autowired
	ProductCategoryService productCategoryService;
	
	@Override
	public void initialize(UniqueProductCategorySlug constraintAnnotation) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public boolean isValid(ProductCategory productCategory, ConstraintValidatorContext context) {
		
		ProductCategory productCategory1 = productCategoryService.getByDeclaration("slug", productCategory.getSlug());
		boolean isValid = productCategory1 == null || (productCategory1.getId().equals(productCategory.getId()) && StringUtils.equals(productCategory1.getSlug(), productCategory.getBeforeSlug()));
        if ( !isValid ) {
        	context.disableDefaultConstraintViolation();
        	context
                    .buildConstraintViolationWithTemplate( "{OnlineMarket.isUniqueProductSlug}" )
                    .addPropertyNode( "slug" ).addConstraintViolation();
        }
		return isValid;

	}

}
