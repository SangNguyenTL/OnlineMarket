package onlinemarket.validation;

import javax.validation.Constraint;
import java.lang.annotation.*;


@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniquePostSlugValidator.class)
public @interface UniquePostSlug {
	
	String message() default "{onlinemarket.validation.UniquePostSlug.message}";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
