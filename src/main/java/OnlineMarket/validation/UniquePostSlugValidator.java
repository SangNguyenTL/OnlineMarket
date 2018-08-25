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
	public boolean isValid(Post post, ConstraintValidatorContext context) {

		Post post1 = postService.getByDeclaration("slug", post.getSlug());
		boolean isValid = post1 == null ||  (post1.getId().equals(post.getId()) && StringUtils.equals(post1.getSlug(), post.getBeforeSlug()));

        if ( !isValid ) {
        	context.disableDefaultConstraintViolation();
        	context
                    .buildConstraintViolationWithTemplate( "{OnlineMarket.isUniquePostSlug}" )
                    .addPropertyNode( "slug" ).addConstraintViolation();
        }
		return isValid;
	}

}
