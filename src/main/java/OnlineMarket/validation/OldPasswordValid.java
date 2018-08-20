package OnlineMarket.validation;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Documented
@Target({ TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OldPasswordValidator.class)
public @interface OldPasswordValid {

    String message() default "The current password isn't valid.";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

}
