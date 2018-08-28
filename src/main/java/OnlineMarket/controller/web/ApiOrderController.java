package OnlineMarket.controller.web;

import OnlineMarket.listener.OnOrderEvent;
import OnlineMarket.model.Order;
import OnlineMarket.result.api.ResponseResult;
import OnlineMarket.result.api.ValidationErrorDTO;
import OnlineMarket.service.OrderService;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.other.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping("/api/order")
public class ApiOrderController {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/change-status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeStatusOrder(@RequestParam("id") Integer id, @RequestParam("status") Byte status, HttpServletRequest request){
        ResponseResult responseResult = new ResponseResult();
        boolean error = false;
        responseResult.setValidationErrorDTO(new ValidationErrorDTO());
        try {

            Order order = orderService.changeStatus(id, status);
            if(Objects.equals(status, OrderStatus.DELIVERING.getId())){
                eventPublisher.publishEvent(new OnOrderEvent(order, "approved", request));
            }

        }catch (CustomException e){
            error = true;
            responseResult.setMessage(e.getMessage());
        }

        responseResult.setError(error);
        return ResponseEntity.ok(responseResult);
    }

}
