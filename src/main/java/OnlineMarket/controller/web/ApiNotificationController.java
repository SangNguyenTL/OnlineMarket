package OnlineMarket.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import OnlineMarket.result.api.ResponseResult;
import OnlineMarket.service.NotificationService;
import OnlineMarket.util.exception.CustomException;

@RestController
@RequestMapping("/api/notification")
public class ApiNotificationController {

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/change-status", method = RequestMethod.GET)
    public ResponseEntity<?> changeStatus(@RequestParam("id") Integer id, @RequestParam("status") Byte status){
        boolean error = false;
        String message = "";
        try {
            notificationService.modifyStatus(id, status);
        } catch (CustomException e) {
            error = true;
            message = e.getMessage();

        }
        return ResponseEntity.ok(new ResponseResult(error, message));
    }

    @RequestMapping(value = "/count", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> countNotification(){

        boolean error = false;
        String message;
        try {
            message = String.valueOf(notificationService.countByUser());
        } catch (CustomException e) {
            error = true;
            message = e.getMessage();
        }

        return ResponseEntity.ok(new ResponseResult(error, message));
    }
}
