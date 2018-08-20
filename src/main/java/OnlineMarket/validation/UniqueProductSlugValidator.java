package OnlineMarket.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import OnlineMarket.form.product.ProductForm;
import OnlineMarket.model.Product;
import OnlineMarket.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Component
public class UniqueProductSlugValidator implements ConstraintValidator<UniqueProductSlug, Object>{

	@Autowired
	ProductService productService;
	
	@Override
	public void initialize(UniqueProductSlug constraintAnnotation) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public boolean isValid(Object candidate, ConstraintValidatorContext context) {

		ProductForm productForm = (ProductForm) candidate;
		Product product = productService.getByDeclaration("slug", productForm.getSlug());
		boolean isValid = false;
		if(product == null || StringUtils.equals(product.getSlug(), productForm.getBeforeSlug())) isValid = true;
		else isValid = false;
        if ( !isValid ) {
        	context.disableDefaultConstraintViolation();
        	context
                    .buildConstraintViolationWithTemplate( "{OnlineMarket.isUniqueProductSlug}" )
                    .addPropertyNode( "slug" ).addConstraintViolation();
        }
		return isValid;
	}

}
