package onlinemarket.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EventValidateValidator.class)
public @interface EventValidate {
    String message() default "{EventValidate.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
