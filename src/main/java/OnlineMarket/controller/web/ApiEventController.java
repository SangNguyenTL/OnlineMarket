package OnlineMarket.controller.web;

import OnlineMarket.form.filter.CheckCode;
import OnlineMarket.model.Event;
import OnlineMarket.model.Product;
import OnlineMarket.result.api.ResponseResult;
import OnlineMarket.result.api.ValidationErrorDTO;
import OnlineMarket.service.EventService;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.other.EventStatus;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/api/event")
public class ApiEventController {

    @Autowired
    EventService eventService;

    @RequestMapping(
            value = "/check-code",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> checkCode(@Valid @RequestBody CheckCode checkCode, BindingResult result){
        boolean error = false;
        List<Event> eventList = new ArrayList<>();
        ResponseResult responseResult = new ResponseResult();
        responseResult.setValidationErrorDTO(new ValidationErrorDTO());
        if(!result.hasErrors()){
            try {
                Event event = eventService.getByDeclaration("code", checkCode.getCode());
                if(event == null) throw new CustomException("Code not found.");
                Calendar calendar = Calendar.getInstance();
                if(calendar.getTime().getTime() - event.getDateTo().getTime() > 0){
                    throw new CustomException("Code is expired!");
                }else if( event.getDateFrom().getTime() - calendar.getTime().getTime() > 0 || !event.getStatus().equals(EventStatus.ACTIVE.getId())){
                    throw new CustomException("Code is invalid");
                }
                int count = 0;
                for(Product product : event.getProducts()){
                    if(ArrayUtils.contains(checkCode.getProductIds(), product.getId()) && product.getPrice() > event.getMinPrice() && product.getPrice() < event.getMaxPrice()) count++;
                }
                if(count == 0 && event.getProducts().size() > 0 ){
                    throw new CustomException("Code is invalid");
                }
                eventList.add(event);
                responseResult.setList(eventList);
            }catch (CustomException e){
                error = true;
                responseResult.getValidationErrorDTO().addFieldError("code", e.getMessage());
            }
        }else{
            for(FieldError fieldError : result.getFieldErrors()){
                responseResult.getValidationErrorDTO().addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
            }
            error = true;
        }


        responseResult.setError(error);
        return ResponseEntity.ok(responseResult);
    }
}
