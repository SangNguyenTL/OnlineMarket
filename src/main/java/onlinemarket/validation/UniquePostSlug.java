package onlinemarket.validation;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({ ElementType.TYPE })
@Constraint(validatedBy = UniquePostSlugValidator.class)
public @interface UniquePostSlug {
	
	String message() default "{onlinemarket.validation.UniquePostSlug.message}";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
