package onlinemarket.validation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;

@Documented
@Retention(RUNTIME)
@Target({ ElementType.TYPE })
@Constraint(validatedBy = UniqueProductCategorySlugValidator.class)
public @interface UniqueProductSlug {
	
	String message() default "{onlinemarket.validation.UniqueProductSlug.message}";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
