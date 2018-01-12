package onlinemarket.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import onlinemarket.model.User;
import onlinemarket.service.ProductCategoryService;
import onlinemarket.service.UserService;
import onlinemarket.service.config.ConfigurationService;
import onlinemarket.util.Slugify;

public abstract class MainController {
	
	@Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;
	
	@Autowired
	protected ConfigurationService configurationService;
	
	@Autowired
	protected ProductCategoryService productCategoryService;
	
	@Autowired
	protected UserService userService;
	
	protected User currentUser;
	
	protected Slugify slg;
	
	@ModelAttribute
	public void populateMetaPage(ModelMap model) {
		model.put("general", configurationService.getGeneral());
		model.put("api", configurationService.getApiConfig());
		model.put("logo", configurationService.getLogo());
		model.put("contact", configurationService.getContag());
		model.put("productCategoryList", productCategoryService.list());
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
	
    @PostConstruct
    public void init() {
       slg = new Slugify().withTransliterator(true).withLowerCase(true);
       requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);
    }
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
