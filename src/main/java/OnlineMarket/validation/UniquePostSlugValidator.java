package OnlineMarket.validation;

import OnlineMarket.model.Post;
import OnlineMarket.service.PostService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniquePostSlugValidator implements ConstraintValidator<UniquePostSlug, Post>{

	@Autowired
	PostService postService;

	@Override
	public void initialize(UniquePostSlug constraintAnnotation) {
	}

	@Override
	public boolean isValid(Post candidate, ConstraintValidatorContext context) {

		Post post = candidate;
		Post post1 = postService.getByDeclaration("slug", post.getSlug());
		boolean isValid = false;
		if(post1 == null || StringUtils.equals(post1.getSlug(), post.getBeforeSlug())) isValid = true;
		else isValid = false;
        if ( !isValid ) {
        	context.disableDefaultConstraintViolation();
        	context
                    .buildConstraintViolationWithTemplate( "{OnlineMarket.isUniquePostSlug}" )
                    .addPropertyNode( "slug" ).addConstraintViolation();
        }
		return isValid;
	}

}
