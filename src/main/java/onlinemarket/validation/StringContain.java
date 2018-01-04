package onlinemarket.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = StringContainValidator.class)
public @interface StringContain {
    String[] acceptedValues();

	String message() default "{onlinemarket.validation.StringContain.message}";

    Class<String>[] groups() default {};

    Class<String>[] payload() default {};
}
