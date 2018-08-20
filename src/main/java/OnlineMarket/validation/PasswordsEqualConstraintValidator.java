package OnlineMarket.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class PasswordsEqualConstraintValidator implements ConstraintValidator<PasswordsEqualConstraint, Object> {

	private String message;
	
	@Override
	public void initialize(PasswordsEqualConstraint arg0) {
		this.message = arg0.message();
	}

	@Override
	public boolean isValid(Object candidate, ConstraintValidatorContext arg1) {

		boolean flag;
        String confirmPassword = "";
        try {
			Field passwordField = candidate.getClass().getDeclaredField("password");
			passwordField.setAccessible(true);
			String password = (String) passwordField.get(candidate);
            Field confirmPasswordField = candidate.getClass().getDeclaredField("confirmPassword");
            confirmPasswordField.setAccessible(true);
            confirmPassword = (String) confirmPasswordField.get(candidate);
			flag = StringUtils.isNotBlank(password) && StringUtils.isNotBlank(confirmPassword) && StringUtils.equals(password, confirmPassword);

		} catch (IllegalAccessException | NoSuchFieldException e) {
			flag = false;
		}

        if ( !flag ) {
		    if(StringUtils.isNotBlank(confirmPassword)){
                arg1.disableDefaultConstraintViolation();
                arg1
                        .buildConstraintViolationWithTemplate( this.message )
                        .addPropertyNode( "confirmPassword" ).addConstraintViolation();
            }
        }
        
        return flag;
	}
}