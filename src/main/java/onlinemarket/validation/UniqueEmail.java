package onlinemarket.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {

	String message() default "{onlinemarket.validation.UniqueEmail.message}";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}