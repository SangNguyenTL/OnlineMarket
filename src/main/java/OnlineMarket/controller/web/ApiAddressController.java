package OnlineMarket.controller.web;

import OnlineMarket.model.Address;
import OnlineMarket.model.User;
import OnlineMarket.result.api.ValidationErrorDTO;
import OnlineMarket.service.AddressService;
import OnlineMarket.result.api.ResponseResult;
import OnlineMarket.service.UserService;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.group.AdvancedValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/address")
public class ApiAddressController {

    @Autowired
    AddressService addressService;

    @Autowired
    UserService userService;

    @RequestMapping(
            value = "/list",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> list(){

        User user = userService.getCurrentUser();
        List<Address> addressList = null;
        if(user != null) addressList = addressService.listByDeclaration("user",user);

        return ResponseEntity.ok(new ResponseResult(false, addressList));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> get(@RequestParam("addressId") Integer addressId){

        boolean error = false;
        User user = userService.getCurrentUser();
        List<Address> addressList = new ArrayList<>();
        try {
            Address address = addressService.getByKeyAndUser(addressId, user);
            addressList.add(address);
        } catch (CustomException e) {
            error = true;
        }

        return ResponseEntity.ok(new ResponseResult(error, addressList));
    }

    @RequestMapping(
            value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> add(@Validated(value = {Default.class, AdvancedValidation.AddNew.class}) @RequestBody Address address, BindingResult result){

        boolean error = true;
        ValidationErrorDTO validationErrorDTO = null;
        if(!result.hasErrors()){
            try {
                addressService.save(address, userService.getByKey(address.getUserId()));
                error = false;
            } catch (CustomException e) {
                validationErrorDTO = new ValidationErrorDTO();
                validationErrorDTO.addFieldError("userId", e.getMessage());
            }
        }else {
            validationErrorDTO = new ValidationErrorDTO();
            for(FieldError fieldError : result.getFieldErrors()){
                validationErrorDTO.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }

        return ResponseEntity.ok(new ResponseResult(error, validationErrorDTO));

    }
}
