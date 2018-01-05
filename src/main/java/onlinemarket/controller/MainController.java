package onlinemarket.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import onlinemarket.model.Province;
import onlinemarket.model.User;
import onlinemarket.model.other.AdvancedValidation;
import onlinemarket.model.other.UserAddressNest;
import onlinemarket.service.AddressService;
import onlinemarket.service.ProvinceService;
import onlinemarket.service.UserService;
import onlinemarket.service.config.ConfigurationService;

@Controller
@RequestMapping("")
public class MainController {

	@Autowired
	ConfigurationService configurationService;

	@Autowired
	ProvinceService provinceService;

	@Autowired
	UserService userService;

	@Autowired
	AddressService addressService;
	
	@Autowired
	PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;
	
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	private User currentUser;

	@ModelAttribute
	public void populateMetaPage(ModelMap model) {
		model.put("general", configurationService.getGeneral());
		model.put("social", configurationService.getSocial());
		model.put("logo", configurationService.getLogo());
		model.put("contact", configurationService.getContag());
	}
	
	@ModelAttribute("currentUser")
	public User getCurrentUser() {
		String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        
        currentUser = userService.getByEmail(userName);
        if(currentUser == null) {
            currentUser = new User();
            currentUser.setEmail(userName);
        } 
        return currentUser;
	}

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public String homePage(ModelMap model) {
		
		model.put("pageTile", "Trang chủ");
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
		model.put("pageTile", "Đăng ký");
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
		model.put("pageTile", "Đăng ký");
		model.put("provinceList", provinceService.list());
		return "frontend/register";

	}

    @RequestMapping(value="/error", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ModelAndView handleError401(HttpServletRequest request)   {
    	
        ModelAndView errorPage = new ModelAndView("backend/error");
        int httpErrorCode = 401;
        errorPage.addObject("pageTitle", httpErrorCode+ " Error");
        errorPage.addObject("errorMsg", "Bạn không có quyền truy cập vào đây!");
        errorPage.addObject("code", httpErrorCode);
        return errorPage;
        
    }
    
}
