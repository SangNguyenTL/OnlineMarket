package onlinemarket.validation;

import java.util.Calendar;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BeforeTodayValidator implements ConstraintValidator<BeforeToday, Date> {

	@Override
	public void initialize(BeforeToday arg0) {		
	}

	@Override
	public boolean isValid(Date arg0, ConstraintValidatorContext arg1) {

		Calendar c = Calendar.getInstance(); 
        c.setTime(arg0); 
        return arg0.before(c.getTime());
	}

}
