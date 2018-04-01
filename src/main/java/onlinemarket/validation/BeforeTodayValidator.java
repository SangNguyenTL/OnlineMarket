package onlinemarket.validation;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BeforeTodayValidator implements ConstraintValidator<BeforeToday, Date> {

	@Override
	public void initialize(BeforeToday arg0) {		
	}

	@Override
	public boolean isValid(Date arg0, ConstraintValidatorContext arg1) {

        return arg0.before(new Date());
	}

}
