package onlinemarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import onlinemarket.model.User;
import onlinemarket.service.UserService;
import onlinemarket.service.config.ConfigurationService;


@Controller
@RequestMapping("/admin")
public class AdminController{

	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private UserService userService;
	
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

	
	@RequestMapping(value = { "" }, method = RequestMethod.GET)
	String homePage(ModelMap model) {
		
		model.put("pageTitle", "Trang quản trị");
		return "backend/index";
	}
	
	@RequestMapping(value = { "menu" }, method = RequestMethod.GET)
	public String menuPage(ModelMap model) {
		
		model.put("pageTitle", "Danh mục");
		return "backend/index";
	}
}