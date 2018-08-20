package OnlineMarket.validation;

import javax.validation.Constraint;
import java.lang.annotation.*;


@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniquePostSlugValidator.class)
public @interface UniquePostSlug {
	
	String message() default "{OnlineMarket.validation.UniquePostSlug.message}";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
