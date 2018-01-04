package onlinemarket.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

@Component
public class IntegerContainValidator implements ConstraintValidator<IntegerContain, Integer>{

    private List<Integer> valueList;

    @Override
    public void initialize(IntegerContain constraintAnnotation) {
        valueList = new ArrayList<Integer>();
        for(int val : constraintAnnotation.acceptedValues()) {
            valueList.add(val);
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if(!valueList.contains(value)) {
            return false;
        }
        return true;
    }

}