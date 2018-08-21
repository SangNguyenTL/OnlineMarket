package OnlineMarket.controller.frontend;

import OnlineMarket.controller.MainController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/check-out")
public class FrCheckOutController extends MainController {


    @ModelAttribute
    public ModelMap modelMap(ModelMap modelMap){
        modelMap.put("pageTitle", "Checkout");
        return modelMap;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(){

        if(currentUser == null){
            return "redirect:/register";
        }

        return "frontend/check-out";

    }

}
