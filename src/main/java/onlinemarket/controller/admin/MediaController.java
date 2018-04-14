package onlinemarket.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import onlinemarket.controller.MainController;

@Controller
@RequestMapping("/admin/media")
public class MediaController extends MainController{

	@ModelAttribute
	public ModelMap populateAttribute(ModelMap model) {
		relativePath = "/admin/media";
		generateBreadcrumbs();
		breadcrumbs.add(new String[]{ relativePath, "Meida"});

		return model;
	}

		@RequestMapping(value = { "" }, method = RequestMethod.GET)
	public String mainPage(ModelMap model) {
		model.put("pageTitle", "Multimedia");
		return "backend/media";
	}
	
}
