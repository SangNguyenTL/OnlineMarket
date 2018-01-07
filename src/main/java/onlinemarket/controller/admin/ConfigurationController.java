package onlinemarket.controller.admin;

import javax.validation.Valid;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import onlinemarket.controller.MainController;
import onlinemarket.form.config.ContactConfig;
import onlinemarket.form.config.EmailSystemConfig;
import onlinemarket.form.config.GeneralConfig;
import onlinemarket.form.config.LogoConfig;
import onlinemarket.form.config.SocialConfig;
import onlinemarket.form.config.UploadConfig;
import onlinemarket.model.User;


@Controller
@RequestMapping("/admin/config")
public class ConfigurationController extends MainController{
	
	@ModelAttribute
	@Override
	public void populateMetaPage(ModelMap model) {
		model.put("general", configurationService.getGeneral());
		model.put("logo", configurationService.getLogo());
		model.put("upload", configurationService.getUpload());
		model.put("contact", configurationService.getContag());
		model.put("pageTitle", "Setting");
		model.put("configPage", true);
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
	public String configPage(@ModelAttribute("general") GeneralConfig general,ModelMap model) {
		model.put("path", "general");
		model.put("subPageTitle", "General");
		model.put("generalConfig", general);
		model.put("description", "Set some of the basic properties of the application.");
		return "backend/config";
	}
	
	@RequestMapping(value = { "" }, method = RequestMethod.POST)
	public String submitConfigPage(@Valid @ModelAttribute("general") GeneralConfig generalConfig, BindingResult result, ModelMap model) {
		model.put("path", "general");
		model.put("subPageTitle", "general");
		model.put("description", "Set some of the basic properties of the application.");
		if(!result.hasErrors()) {
			configurationService.saveGeneralConfig(generalConfig);
		}
		return "redirect:/admin/config?success";
	}
	
	@RequestMapping(value = { "upload" }, method = RequestMethod.GET)
	public String configUploadPage(@ModelAttribute("upload") UploadConfig upload, ModelMap model) {
		model.put("path", "upload");
		model.put("subPageTitle", "Upload");
		model.put("description", "Set upload limits.");
		model.put("uploadConfig", upload);
		return "backend/config";
		
	}
	
	@RequestMapping(value = { "upload" }, method = RequestMethod.POST)
	public String submitUploadPage(@ModelAttribute("uploadConfig") UploadConfig upload, BindingResult result, ModelMap model) {
		model.put("path", "upload");
		model.put("subPageTitle", "Upload");
		model.put("description", "Set upload limits.");
		if(!result.hasErrors()) {
			configurationService.saveUploadconfig(upload);
		}
		return "redirect:/admin/config/upload?success";
	}
	
	@RequestMapping(value = { "logo" }, method = RequestMethod.GET)
	public String configLogoPage(@ModelAttribute("logo") LogoConfig logo, ModelMap model) {
		model.put("path", "logo");
		model.put("subPageTitle", "Logo");
		model.put("description", "Set the icon of the app as well as the store brand.");
		model.put("logoConfig", logo);
		return "backend/config"; 
	}
	
	@RequestMapping( value = {"contact"}, method = RequestMethod.GET)
	public String configContactPage(@ModelAttribute("contact") ContactConfig contact, ModelMap model) {
		model.put("path", "contact");
		model.put("subPageTitle", "Contact");
		model.put("contactConfig", contact);
		model.put("description", "Setting up a store address helps customers understand where the store is located.");
		return "backend/config";
	}
	
	@RequestMapping( value = {"contact"}, method = RequestMethod.POST)
	public String submitContactPage(@ModelAttribute("contactConfig") ContactConfig contact,BindingResult result, ModelMap model) {
		
		model.put("path", "contact");
		model.put("subPageTitle", "Contact");
		model.put("description", "Setting up a store address helps customers understand where the store is located.");
		if(!result.hasErrors()) {
			configurationService.saveContactConfig(contact);
		}
		return "redirect:/admin/config/contact?success";
	}
	
	@RequestMapping( value = {"social"}, method = RequestMethod.GET)
	public String configSocialPage(@ModelAttribute("social") SocialConfig social, ModelMap model) {
		
		model.put("path", "social");
		model.put("subPageTitle", "Social Network");
		model.put("description", "Setting the URL of the page of social network, show the network networks of applications.");
		model.put("socialConfig", social);
		
		return "backend/config";
	}  
	
	@RequestMapping( value = {"social"}, method = RequestMethod.POST)
	public String submitSocialPage(@ModelAttribute("socialConfig") SocialConfig social,BindingResult result, ModelMap model) {
		
		model.put("path", "social");
		model.put("subPageTitle", "Social Network");
		model.put("description", "Set the URL of the social network page, which shows social networking applications.");
		if(!result.hasErrors()) {
			configurationService.saveSocialConfig(social);
		}
		return "redirect:/admin/config/social?success";
	}	
	
	@RequestMapping( value = {"emailsystem"}, method = RequestMethod.GET)
	public String configEmailSystemPage(@ModelAttribute("email") EmailSystemConfig email, ModelMap model) {
		
		model.put("path", "emailsystem");
		model.put("subPageTitle", "Email System");
		model.put("description", "Set parameters so that the application can send the message to the client.");
		model.put("emailSystemConfig", configurationService.getEmail());
		
		return "backend/config";
	}
	
	@RequestMapping( value = {"emailsystem"}, method = RequestMethod.POST)
	public String submitEmailSystemPage(@ModelAttribute("emailSystemConfig") EmailSystemConfig email, BindingResult result, ModelMap model) {
		model.put("path", "emailsystem");
		model.put("subPageTitle", "Email System");
		model.put("description", "Set parameters so that the application can send the message to the client.");
		if(!result.hasErrors()) {
			configurationService.saveEmailSystemConfig(email);
		}
		return "redirect:/admin/config/emailsystem?success";
	}
}
