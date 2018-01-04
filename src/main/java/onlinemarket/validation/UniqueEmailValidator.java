package onlinemarket.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import onlinemarket.model.User;
import onlinemarket.service.UserService;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String>{

	@Autowired
	UserService userService;
	
	@Override
	public void initialize(UniqueEmail arg0) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public boolean isValid(String arg0, ConstraintValidatorContext arg1) {
		User user = userService.getByEmail(arg0);
		return user == null;
	}

}
