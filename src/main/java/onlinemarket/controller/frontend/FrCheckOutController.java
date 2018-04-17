package onlinemarket.controller.frontend;

import onlinemarket.controller.MainController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/check-out")
public class FrCheckOutController extends MainController {


    @ModelAttribute
    public ModelMap modelMap(ModelMap modelMap){

        modelMap.put("title", "Checkout");

        return modelMap;
    };

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(ModelMap modelMap){

        return "frontend/check-out";

    }

}
