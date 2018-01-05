package onlinemarket.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import onlinemarket.model.User;
import onlinemarket.service.UserService;
import onlinemarket.service.config.ConfigurationService;

@Controller
@RequestMapping("/admin/media")
public class MediaController {
	
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private UserService userService;
	
	private User currentUser;

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	@ModelAttribute
	public void populateMetaPage(ModelMap model) {
		model.put("general", configurationService.getGeneral());
		model.put("logo", configurationService.getLogo());
		model.put("upload", configurationService.getUpload());
		model.put("contact", configurationService.getContag());
		model.put("pageTitle", "Thiết lập");
		model.put("mediaPage", true);
		model.put("social", configurationService.getSocial());
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
	public String mainPage(ModelMap model) {
		
<<<<<<< HEAD
		model.put("pageTitle", "Đa phương tiện");
=======
		return "backend/media";
	}
}
