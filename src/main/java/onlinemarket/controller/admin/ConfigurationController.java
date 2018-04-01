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
import onlinemarket.form.config.ApiConfig;
import onlinemarket.form.config.ContactConfig;
import onlinemarket.form.config.EmailSystemConfig;
import onlinemarket.form.config.GeneralConfig;
import onlinemarket.form.config.LogoConfig;
import onlinemarket.form.config.SocialConfig;
import onlinemarket.form.config.UploadConfig;
import onlinemarket.model.User;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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
		model.put("api", configurationService.getApiConfig());
		model.put("social", configurationService.getSocial());
		model.put("pageTitle", "Setting");
		model.put("configPage", true);
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
	public String submitConfigPage(@Valid @ModelAttribute("general") GeneralConfig generalConfig, BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {
		
		model.put("path", "general");
		model.put("subPageTitle", "general");
		model.put("description", "Set some of the basic properties of the application.");
		if(!result.hasErrors()) {
			configurationService.saveGeneralConfig(generalConfig);
			redirectAttributes.addFlashAttribute("success", true);
			return "redirect:/admin/config";
		}
		
		return "backend/config";
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
	public String submitUploadPage(@ModelAttribute("uploadConfig") UploadConfig upload, BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {
		model.put("path", "upload");
		model.put("subPageTitle", "Upload");
		model.put("description", "Set upload limits.");
		if(!result.hasErrors()) {
			configurationService.saveUploadconfig(upload);
			redirectAttributes.addFlashAttribute("success", true);
			return "redirect:/admin/config/upload";
		}
		return "backend/config";
	}
	
	@RequestMapping(value = { "logo" }, method = RequestMethod.POST)
	public String configLogoPage(@ModelAttribute("logo") @Valid LogoConfig logo, BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {
		
		if(!result.hasErrors()) {
			configurationService.saveLogoConfig(logo);
			redirectAttributes.addFlashAttribute("success", true);
			return "redirect:/admin/config/logo";
		}
		
		model.put("path", "logo");
		model.put("subPageTitle", "Logo");
		model.put("description", "Set the icon of the app as well as the store brand.");
		model.put("logoConfig", logo);
		
		
		return "backend/config"; 
	}
	
	@RequestMapping(value = { "logo" }, method = RequestMethod.GET)
	public String processLogoPage(@ModelAttribute("logo") LogoConfig logo, ModelMap model) {
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
	public String submitContactPage(@ModelAttribute("contactConfig") ContactConfig contact,BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {
		
		model.put("path", "contact");
		model.put("subPageTitle", "Contact");
		model.put("description", "Setting up a store address helps customers understand where the store is located.");
		if(!result.hasErrors()) {
			configurationService.saveContactConfig(contact);
			redirectAttributes.addFlashAttribute("success", true);
			return "redirect:/admin/config/contact";
		}
		
		return "backend/config";
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
	public String submitSocialPage(@ModelAttribute("socialConfig") SocialConfig social,BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {
		
		model.put("path", "social");
		model.put("subPageTitle", "Social Network");
		model.put("description", "Set the URL of the social network page, which shows social networking applications.");
		if(!result.hasErrors()) {
			configurationService.saveSocialConfig(social);
			redirectAttributes.addFlashAttribute("success", true);
			return "redirect:/admin/config/social";
		}
		
		return "backend/config";
	}	
	
	@RequestMapping( value = {"emailsystem"}, method = RequestMethod.GET)
	public String configEmailSystemPage(@ModelAttribute("email") EmailSystemConfig email, ModelMap model) {
		
		model.put("path", "emailsystem");
		model.put("subPageTitle", "Email System");
		model.put("description", "Set parameters so that the application can send the message to the client.");
		model.put("emailSystemConfig", email);
		
		return "backend/config";
	}
	
	@RequestMapping( value = {"emailsystem"}, method = RequestMethod.POST)
	public String submitEmailSystemPage(@ModelAttribute("emailSystemConfig") EmailSystemConfig email, BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {
		model.put("path", "emailsystem");
		model.put("subPageTitle", "Email System");
		model.put("description", "Set parameters so that the application can send the message to the client.");
		if(!result.hasErrors()) {
			configurationService.saveEmailSystemConfig(email);
			redirectAttributes.addFlashAttribute("success", true);
			return "redirect:/admin/config/emailsyste";
		}
		return "backend/config";
	}

	@RequestMapping( value = {"api"}, method = RequestMethod.GET)
	public String configApiPage(@ModelAttribute("api") ApiConfig api, ModelMap model) {
		
		model.put("path", "api");
		model.put("subPageTitle", "Api System");
		model.put("description", "Set api systems for applications");
		model.put("apiConfig", api);
		
		return "backend/config";
	}
	
	@RequestMapping( value = {"api"}, method = RequestMethod.POST)
	public String submitApiPage(@ModelAttribute("apiConfig") ApiConfig api, BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {
		model.put("path", "api");
		model.put("subPageTitle", "Api System");
		model.put("description", "Set api systems for applications");
		if(!result.hasErrors()) {
			configurationService.saveApiConfig(api);
			redirectAttributes.addFlashAttribute("success", true);
			return "redirect:/admin/config/api";
		}
		return "backend/config";
	}
}
