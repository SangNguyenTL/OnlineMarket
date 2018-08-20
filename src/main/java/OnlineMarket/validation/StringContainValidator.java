package OnlineMarket.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StringContainValidator implements ConstraintValidator<StringContain, String>{

    private List<String> valueList;

    @Override
    public void initialize(StringContain constraintAnnotation) {
        valueList = new ArrayList<>();
        for(String val : constraintAnnotation.acceptedValues()) {
            valueList.add(val.toLowerCase());
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value instanceof  String)
            return valueList.contains(value.toLowerCase());
        else return false;
    }

}