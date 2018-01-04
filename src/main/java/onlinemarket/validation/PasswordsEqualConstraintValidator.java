package onlinemarket.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import onlinemarket.model.User;
import onlinemarket.model.other.UserAddressNest;

@Component
public class PasswordsEqualConstraintValidator implements ConstraintValidator<PasswordsEqualConstraint, Object> {

	private String message;
	
	@Override
	public void initialize(PasswordsEqualConstraint arg0) {
		this.message = arg0.message();
	}

	@Override
	public boolean isValid(Object candidate, ConstraintValidatorContext arg1) {
	    UserAddressNest nest = (UserAddressNest) candidate;
	    User user = nest.getUser();
	    if(user.getPassword() == null) return true;
	    boolean isValid = user.getPassword().equals(nest.getConfirmPassword());

        if ( !isValid ) {
        	arg1.disableDefaultConstraintViolation();
        	arg1
                    .buildConstraintViolationWithTemplate( this.message )
                    .addPropertyNode( "confirmPassword" ).addConstraintViolation();
        }
        
        return isValid;
	}
}