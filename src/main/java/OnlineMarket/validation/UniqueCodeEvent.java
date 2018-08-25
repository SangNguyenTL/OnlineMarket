package OnlineMarket.validation;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({ ElementType.TYPE })
@Constraint(validatedBy = UniqueCodeEventValidator.class)
public @interface UniqueCodeEvent {
	
	String message() default "{OnlineMarket.validation.UniqueCodeEvent.message}";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
