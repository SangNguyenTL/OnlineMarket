package OnlineMarket.controller;

import OnlineMarket.listener.OnRegistrationCompleteEvent;
import OnlineMarket.model.PasswordResetToken;
import OnlineMarket.util.other.State;
import OnlineMarket.model.User;
import OnlineMarket.model.VerificationToken;
import OnlineMarket.util.group.AdvancedValidation;
import OnlineMarket.service.ProductService;
import OnlineMarket.service.ProvinceService;
import OnlineMarket.service.SendMailConstruction;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.exception.PasswordResetTokenExistingException;
import OnlineMarket.util.exception.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.MailSendException;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.exceptions.TemplateProcessingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.groups.Default;
import java.util.Calendar;
import java.util.UUID;

@Controller
@RequestMapping("")
public class RegisterAndLoginController extends MainController{

    @Autowired
    ProductService productService;

    @Autowired
    ProvinceService provinceService;

    @Autowired
    SendMailConstruction sendMailConstruction;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(ModelMap model, HttpSession session) {
        breadcrumbs.add(new String[]{"/login", "Login"});

        if (currentUser != null)
            return "redirect:/";
        model.put("pageTitle", "Login");
        if(session != null){
            SavedRequest savedRequest = (SavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
            String redirect;
            if(savedRequest!= null)
                redirect =  savedRequest.getRedirectUrl();
            else
                redirect = "/";
            model.put("redirect", redirect);
        }

        return "frontend/login";

    }

    @RequestMapping(value = { "/register" }, method = RequestMethod.GET)
    public String registerPage(ModelMap model) {

        if (currentUser.getId() != null)
            return "redirect:/";

        breadcrumbs.add(new String[]{"/register", "Registration"});
        model.put("pageTitle", "Registration");
        model.put("provinceList", provinceService.list());
        model.put("user", new User());

        return "frontend/register";

    }

    @RequestMapping(value = { "/register" }, method = RequestMethod.POST)
    public String processRegister(@ModelAttribute("user") @Validated(value = { Default.class,
            AdvancedValidation.CheckEmail.class, AdvancedValidation.Register.class })User user, BindingResult result, HttpServletRequest request, ModelMap model) {

        // User and address are valid.
        if (!result.hasErrors()) {

            userService.save(user);

            // Call event after register account
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request));

            model.put("messageTitle", "Registration successful!");
            model.put("message", "To be able to log in, please check your email box for the activation link.");
            return "frontend/404";
        }
        breadcrumbs.add(new String[]{"/register", "Registration"});
        model.put("pageTitle", "Registration");
        model.put("provinceList", provinceService.list());

        return "frontend/register";

    }


    @RequestMapping(value = "/registration-confirm", method = {RequestMethod.GET, RequestMethod.POST})
    public String confirmRegistration
            (ModelMap model, @RequestParam("token") String token, RedirectAttributes redirectAttributes) {

        breadcrumbs.add(new String[]{"/registration-confirm", "Registration Confirm"});
        model.put("pageTitle", "Registration Confirm");

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            model.put("messageTitle", "Invalid Token");
            model.put("message", "Sorry!<br> Your token is invalid, please check your email again or contact us for assistance as soon as possible.");

            return "frontend/404" ;
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            model.put("messageTitle", "Token expired");
            model.put("message", "Sorry!<br> Your token is expired, please contact us for assistance as soon as possible.");
            return "frontend/404";
        }

        user.setState(State.ACTIVE.toString());
        userService.saveOriginal(user);
        userService.deleteVerificationToken(verificationToken);
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/login";
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
    public String resetPasswordPage(ModelMap modelMap) {
        breadcrumbs.add(new String[]{"/forgot-password", "Forgot password"});
        modelMap.put("pageTitle", "Forgot password");

        return "frontend/forgot-password";
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    public String resetPassword(HttpServletRequest request, @RequestParam("email") String email, @RequestParam(value = "resend", required = false) boolean resend,RedirectAttributes redirectAttributes){

        try{

            User user = userService.getByEmail(email);
            if(user == null) throw new UserNotFoundException();
            Calendar cal = Calendar.getInstance();
            PasswordResetToken passwordResetToken = userService.getPasswordResetTokenByUser(user);
            if(passwordResetToken != null){
                if(resend && passwordResetToken.getExpiryDate().getTime() - cal.getTime().getTime() <= 0){
                    passwordResetToken = userService.generateNewPasswordResetToken(passwordResetToken.getToken());
                    sendMailConstruction.sendResetTokenEmail(request, passwordResetToken.getToken(), user);
                }else throw new PasswordResetTokenExistingException("Password reset token is sent to your email and unexpired. You can check the resend email box and submit request again.");

            }else{
                String token = UUID.randomUUID().toString();
                userService.createPasswordResetTokenForUser(user, token);
                sendMailConstruction.sendResetTokenEmail(request, token, user);
            }
            redirectAttributes.addFlashAttribute("success", true);

        }catch (UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("error", "Email isn't registered.");
        } catch (PasswordResetTokenExistingException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("resend", true);
        } catch (MailSendException | MessagingException | TemplateProcessingException e) {
            redirectAttributes.addFlashAttribute("error", "Unknown Error!");
        }

        return "redirect:/forgot-password";
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.GET)
    public String changePasswordPage(@RequestParam("id") int userId, @RequestParam("token") String token, ModelMap modelMap){
        boolean show = false;
        try {
            modelMap.put("pageTitle", "Change Password");
            breadcrumbs.add(new String[]{"/change-password", "Change Password"});
            modelMap.put("id", userId);
            modelMap.put("token", token);
            User user = userService.getByKey(userId);
            if(user == null) throw new UserNotFoundException();
            PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);
            if(passwordResetToken == null) throw new CustomException("Password reset token not found.");
            if(!passwordResetToken.getUser().getId().equals(user.getId())) throw new CustomException("Password reset token is invalid.");
            Calendar calendar = Calendar.getInstance();
            if(passwordResetToken.getExpiryDate().getTime() - calendar.getTime().getTime() <= 0) throw new CustomException("Password reset token is expired");
            show = true;

        } catch (UserNotFoundException | CustomException e) {
            modelMap.put("error", e.getMessage());
        }
        modelMap.put("show", show);

        return "frontend/change-password";
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public String processChangePasswordPage(@RequestParam("id") int userId, @RequestParam("token") String token, @RequestParam("password") String password, @RequestParam("confirmPassword")  String confirmPassword,ModelMap modelMap, RedirectAttributes redirectAttributes){

        boolean show = false;
        try {
            modelMap.put("pageTitle", "Change Password");
            breadcrumbs.add(new String[]{"/change-password", "Change Password"});
            modelMap.put("id", userId);
            modelMap.put("token", token);

            User user = userService.getByKey(userId);
            if(user == null) throw new UserNotFoundException();
            PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);
            if(passwordResetToken == null) throw new CustomException("Password reset token not found.");
            if(!passwordResetToken.getUser().getId().equals(user.getId())) throw new CustomException("Password reset token is invalid.");
            Calendar calendar = Calendar.getInstance();
            if(passwordResetToken.getExpiryDate().getTime() - calendar.getTime().getTime() <= 0) throw new CustomException("Password reset token is expired");
            if(password.length() < 6 || password.length() > 60){
                show = true;
                throw new CustomException("The password must be at least 6 characters. but more not than 60 characters. Re-enter!");
            }
            if(!password.equals(confirmPassword)){
                show = true;
                throw new CustomException("The password does not match. Re-enter!");
            }

            show = true;
            user.setPassword(password);
            userService.update(user,true);
            userService.deletePasswordResetToken(passwordResetToken);
            redirectAttributes.addFlashAttribute("success", "Changing password success. You can login to the system now.");

            return "redirect:/login";

        } catch (UserNotFoundException | CustomException e) {
            modelMap.put("error", e.getMessage());
        }
        modelMap.put("show", show);
        return "frontend/change-password";
    }
}
