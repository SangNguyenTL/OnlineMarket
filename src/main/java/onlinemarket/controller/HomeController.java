package onlinemarket.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import onlinemarket.model.Province;
import onlinemarket.model.other.AdvancedValidation;
import onlinemarket.model.other.UserAddressNest;
import onlinemarket.service.ProvinceService;

@Controller
@RequestMapping("")
public class HomeController extends MainController{

	@Autowired
	ProvinceService provinceService;
	
	@Autowired
	PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public String homePage(ModelMap model) {
		
		model.put("pageTitle", "Home");
		return "frontend/index";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(ModelMap model) {
		
		if (currentUser.getId() == null) {
			model.put("pageTile", "Login");
			return "frontend/login";
	    } else {
	    	return "redirect:/";  
	    }
		
	}

	@RequestMapping(value = { "/register" }, method = RequestMethod.GET)
	public String registerPage(ModelMap model) {
		model.put("pageTitle", "Resiter");
		model.put("provinceList", provinceService.list());
		model.put("nest", new UserAddressNest());
		return "frontend/register";
	}

	@RequestMapping(value = { "/register" }, method = RequestMethod.POST)
	public String processRegister(@ModelAttribute("nest") @Validated(value = {Default.class, AdvancedValidation.CheckEmail.class}) UserAddressNest nest, BindingResult result, ModelMap model) {
		
		if (!result.hasErrors() && nest.getAgree() != null && nest.getAgree().equals("1")) {
			userService.register(nest.getUser(), nest.getAddress());
			return "redirect:/login?success";
		}
		if(nest.getAgree() == null) nest.setAgree("2");
		if(nest.getAddress().getProvince()==null) nest.getAddress().setProvince(new Province());
		model.put("pageTitle", "Register");
		model.put("provinceList", provinceService.list());
		return "frontend/register";
 
	}

    @RequestMapping(value="/error", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ModelAndView handleError401(HttpServletRequest request)   {
        ModelAndView errorPage = new ModelAndView("backend/error");
        int httpErrorCode = 401;
        errorPage.addObject("pageTitle", httpErrorCode+ " Error");
        errorPage.addObject("errorMsg", "You do not have permission to access here!");
        errorPage.addObject("code", httpErrorCode);
        return errorPage;
        
    }
    
}
