package OnlineMarket.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/user/{userId:\\d+}")
public class UserProfileController extends UserControllerInterface {


    @RequestMapping({""})
    public String mainPage(){
        return "user/profile";
    }

    @Override
    public ModelMap modelMap(ModelMap modelMap) {
        modelMap.put("pageTitle", user.getDisplayName() + "| User Profile");
        modelMap.put("profilePage", true);
        return modelMap;
    }
}
