package OnlineMarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class AdminController extends MainController {

	@ModelAttribute
	public ModelMap modelAttribute(ModelMap model) {
		return model;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String mainPage(ModelMap model) {

		model.put("dashboardPage", true);
		model.put("pageTitle", "Administrator Page");
		return "backend/index";

	}

	@RequestMapping(value = { "menu" }, method = RequestMethod.GET)
	public String menuPage(ModelMap model) {

		model.put("pageTitle", "Menu");
		return "backend/index";
	}

}