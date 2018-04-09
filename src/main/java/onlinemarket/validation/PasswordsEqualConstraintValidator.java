package onlinemarket.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import onlinemarket.model.User;

@Component
public class PasswordsEqualConstraintValidator implements ConstraintValidator<PasswordsEqualConstraint, Object> {

	private String message;
	
	@Override
	public void initialize(PasswordsEqualConstraint arg0) {
		this.message = arg0.message();
	}

	@Override
	public boolean isValid(Object candidate, ConstraintValidatorContext arg1) {
	    User user = (User) candidate;
	    if(user.getPassword() == null) return false;
	    boolean isValid = user.getPassword().equals(user.getConfirmPassword());

        if ( !isValid ) {
        	arg1.disableDefaultConstraintViolation();
        	arg1
                    .buildConstraintViolationWithTemplate( this.message )
                    .addPropertyNode( "confirmPassword" ).addConstraintViolation();
        }
        
        return isValid;
	}
}