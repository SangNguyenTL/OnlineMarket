package onlinemarket.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;

@Documented
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
@Constraint(validatedBy = UniqueBrandSlugValidator.class)
public @interface UniqueBrandSlug {
	
	String message() default "{onlinemarket.validation.UniqueSlugBrand.message}";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
