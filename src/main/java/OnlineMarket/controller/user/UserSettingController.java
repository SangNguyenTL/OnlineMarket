package OnlineMarket.controller.user;

import OnlineMarket.form.user.ChangePassword;
import OnlineMarket.model.User;
import OnlineMarket.util.group.AdvancedValidation;
import OnlineMarket.util.exception.CustomException;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.groups.Default;

@Controller
@RequestMapping("/user/{userId:\\d+}/setting")
public class UserSettingController extends UserControllerInterface {

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String settingPageHasUserId(){
        return "user/setting";
    }

    @RequestMapping(value = {""}, method = RequestMethod.POST)
    public String processSettingPageHasUserId(
            @Validated({Default.class}) @ModelAttribute("userForm") User user, BindingResult result,
            ModelMap modelMap, HttpServletRequest request, RedirectAttributes redirectAttributes) throws NoHandlerFoundException {

        if(!result.hasErrors()){
            redirectAttributes.addFlashAttribute("successProfile", true);
            redirectAttributes.addFlashAttribute("settingProfile", true);
            try {
                userService.update(user, false);
            } catch (CustomException e) {
                throw new NoHandlerFoundException(request.getMethod(), request.getRequestURL().toString(), null);
            }
            return "redirect:"+relativePath;
        }

        modelMap.put("settingProfile", true);
        modelMap.put("userForm", user);
        return "user/setting";
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.GET)
    public  String changePassword(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("settingSecurity", true);
        return "redirect:"+relativePath;
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public  String changePassword(@Valid @ModelAttribute("changePassword") ChangePassword changePassword,
                                  BindingResult result,
                                  @RequestParam(value = "flagReset", required = false ) boolean flagReset,
                                  HttpServletRequest request,
                                  ModelMap modelMap, RedirectAttributes redirectAttributes) throws NoHandlerFoundException {

        if(!result.hasErrors()){
            redirectAttributes.addFlashAttribute("successSecurity", true);
            redirectAttributes.addFlashAttribute("settingSecurity", true);
            try {
                userService.changePass(user, changePassword.getPassword());
            } catch (CustomException e) {
                throw new NoHandlerFoundException(request.getMethod(), request.getRequestURL().toString(), null);
            }
            return "redirect:"+relativePath;
        }

        modelMap.put("changePassword", changePassword);
        modelMap.put("settingSecurity", true);

        return "user/setting";
    }

    @Override
    public ModelMap modelMap(ModelMap modelMap) {

        relativePath = userProfilePath +"/setting";
        modelMap.put("pageTitle", "User Profile Setting");
        breadcrumbs.add(new String[]{relativePath, "Setting"});
        modelMap.put("userForm", user);
        modelMap.put("changePassword", new ChangePassword());
        modelMap.put("pathActionChangePass", relativePath+"/change-password");
        modelMap.put("pathAction", relativePath);
        modelMap.put("relativePath", relativePath);
        modelMap.put("settingPage", true);

        return modelMap;
    }
}
