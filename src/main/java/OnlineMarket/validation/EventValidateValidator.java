package OnlineMarket.validation;

import OnlineMarket.model.Event;
import OnlineMarket.service.EventService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class EventValidateValidator implements ConstraintValidator<EventValidate, Event> {

    @Autowired
    EventService eventService;

    @Override
    public void initialize(EventValidate eventValidate) {

    }

    @Override
    public boolean isValid(Event event, ConstraintValidatorContext context) {
        boolean hasError = false;
        if(event.getDateFrom() != null && event.getDateFrom().after(event.getDateTo())){
            context.disableDefaultConstraintViolation();
            hasError = true;
            context.buildConstraintViolationWithTemplate( "Start date must be after end date." )
                    .addPropertyNode( "dateFrom" ).addConstraintViolation();
            context.buildConstraintViolationWithTemplate( "End date must be before start date." )
                    .addPropertyNode( "dateTo" ).addConstraintViolation();
        }


        if(StringUtils.isBlank(event.getCode()) && (event.getProducts() == null || event.getProducts().isEmpty())){
            if(!hasError) context.disableDefaultConstraintViolation();
            hasError = true;
            context.buildConstraintViolationWithTemplate( "Code or product must be fill." )
                    .addPropertyNode( "code" ).addConstraintViolation();
            context.buildConstraintViolationWithTemplate( "Code or product must be fill." )
                    .addPropertyNode( "products" ).addConstraintViolation();
        }

        Event eventOld = eventService.getByDeclaration("code", event.getCode());
        if(eventOld != null){
            if(event.getId() != null && !eventOld.getId().equals(event.getId())){
                if(!hasError) context.disableDefaultConstraintViolation();
                else hasError = true;
                context.buildConstraintViolationWithTemplate( "Code is exist." )
                        .addPropertyNode( "code" ).addConstraintViolation();
            }
        }

        if(event.getPercentValue() != null && event.getValue() != null){
            if(!hasError) context.disableDefaultConstraintViolation();
            hasError = true;
            context.buildConstraintViolationWithTemplate( "Only accept percentages or decreases by value." )
                    .addPropertyNode( "value" ).addConstraintViolation();
            context.buildConstraintViolationWithTemplate( "Only accept percentages or decreases by value." )
                    .addPropertyNode( "percentValue" ).addConstraintViolation();
        }

        if(event.getPercentValue() == null && event.getValue() == null){
            if(!hasError) context.disableDefaultConstraintViolation();
            hasError = true;
            context.buildConstraintViolationWithTemplate( "Please enter sale or percent discount." )
                    .addPropertyNode( "value" ).addConstraintViolation();
            context.buildConstraintViolationWithTemplate( "Please enter sale or percent discount." )
                    .addPropertyNode( "percentValue" ).addConstraintViolation();
        }

        if(event.getMinPrice() != null && event.getMaxPrice() != null && event.getMaxPrice() <= event.getMinPrice()){
            if(!hasError) context.disableDefaultConstraintViolation();
            hasError = true;
            context.buildConstraintViolationWithTemplate( "The max price must be lager than the min price." )
                    .addPropertyNode( "maxPrice" ).addConstraintViolation();
            context.buildConstraintViolationWithTemplate( "The min price must be smaller than the max price." )
                    .addPropertyNode( "minPrice" ).addConstraintViolation();
        }

        return !hasError;
    }
}
