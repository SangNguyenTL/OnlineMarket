package onlinemarket.validation;

import onlinemarket.model.Post;
import onlinemarket.model.Product;
import onlinemarket.service.PostService;
import onlinemarket.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniquePostSlugValidator implements ConstraintValidator<UniqueProductSlug, Object>{

	@Autowired
	PostService postService;
	
	@Override
	public void initialize(UniqueProductSlug constraintAnnotation) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public boolean isValid(Object candidate, ConstraintValidatorContext context) {
		
		Post post = (Post) candidate;
		Post post1 = postService.getByDeclaration("slug", post.getSlug());
		boolean isValid = false;
		if(post1 == null || StringUtils.equals(post1.getSlug(), post.getBeforeSlug())) isValid = true;
		else isValid = false;
        if ( !isValid ) {
        	context.disableDefaultConstraintViolation();
        	context
                    .buildConstraintViolationWithTemplate( "{onlinemarket.isUniquePostSlug}" )
                    .addPropertyNode( "slug" ).addConstraintViolation();
        }
		return isValid;
	}

}
