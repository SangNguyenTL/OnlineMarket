package OnlineMarket.controller.web;

import OnlineMarket.model.User;
import OnlineMarket.service.UserService;
import OnlineMarket.result.api.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class ApiUserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/check-email", produces = MediaType.APPLICATION_JSON_VALUE, method = { RequestMethod.POST,
            RequestMethod.GET })
    public ResponseEntity<?> checkSlugUnique(@RequestParam(value = "value", required = true) String value,
                                             @RequestParam(value = "id", required = false) Integer id) {

        User user = userService.getByDeclaration("email", value);
        boolean flag = true;
        if (user == null)
            flag = false;
        else if(id != null){
            User oldUser = userService.getByKey(id);
            if (oldUser != null && oldUser.equals(user))
                flag = false;
        }
        return ResponseEntity.ok(new ResponseResult(flag, ""));
    }

}
