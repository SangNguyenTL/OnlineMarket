package onlinemarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
@RequestMapping("/admin")
public class AdminController extends MainController{
	
	@RequestMapping(value = { "" }, method = RequestMethod.GET)
	String homePage(ModelMap model) {
		
		model.put("pageTitle", "Aministrator Page");
		return "backend/index";
	}
	
	@RequestMapping(value = { "menu" }, method = RequestMethod.GET)
	public String menuPage(ModelMap model) {
		
		model.put("pageTitle", "Menu");
		return "backend/index";
	}
}