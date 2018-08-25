package OnlineMarket.validation;

import OnlineMarket.model.Event;
import OnlineMarket.service.EventService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueCodeEventValidator implements ConstraintValidator<UniqueCodeEvent, Event>{

	@Autowired
	EventService eventService;
	
	@Override
	public void initialize(UniqueCodeEvent constraintAnnotation) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public boolean isValid(Event event, ConstraintValidatorContext context) {

		Event eventEdited = eventService.getByDeclaration("code", event.getCode());
		boolean isValid = eventEdited == null || (eventEdited.getId().equals(event.getId()) && StringUtils.equals(eventEdited.getCode(), event.getBeforeCode()));
        if ( !isValid ) {
        	context.disableDefaultConstraintViolation();
        	context
                    .buildConstraintViolationWithTemplate( "{OnlineMarket.isUniqueBrandSlug}" )
                    .addPropertyNode( "code" ).addConstraintViolation();
        }
		return isValid;
	}

}
