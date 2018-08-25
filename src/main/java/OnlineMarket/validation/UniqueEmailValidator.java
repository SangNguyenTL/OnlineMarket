package OnlineMarket.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import OnlineMarket.model.User;
import OnlineMarket.service.UserService;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, User>{

	@Autowired
	UserService userService;

	private String message;
	
	@Override
	public void initialize(UniqueEmail arg0) {
		message = arg0.message();
	}

	@Override
	public boolean isValid(User user, ConstraintValidatorContext context) {
		User user1 = userService.getByEmail(user.getEmail());
		boolean isValid = user1 == null || (user1.getId().equals(user.getId()) && user.getEmail().equals(user1.getEmail()));

		if(!isValid){
			context.disableDefaultConstraintViolation();
			context
					.buildConstraintViolationWithTemplate( this.message )
					.addPropertyNode( "email" ).addConstraintViolation();
		}

		return isValid;
	}

}
