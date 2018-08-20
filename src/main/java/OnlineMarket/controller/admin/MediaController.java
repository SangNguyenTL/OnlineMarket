package OnlineMarket.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import OnlineMarket.controller.MainController;

@Controller
@RequestMapping("/admin/media")
public class MediaController extends MainController{

	@ModelAttribute
	public ModelMap modelAttribute(ModelMap model) {
		relativePath = "/admin/media";
		breadcrumbs.add(new String[]{ relativePath, "Meida"});

		return model;
	}

		@RequestMapping(value = { "" }, method = RequestMethod.GET)
	public String mainPage(ModelMap model) {
		model.put("pageTitle", "Multimedia");
		return "backend/media";
	}
	
}
