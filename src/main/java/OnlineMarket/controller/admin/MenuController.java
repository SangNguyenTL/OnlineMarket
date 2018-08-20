package OnlineMarket.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import OnlineMarket.controller.MainController;
import OnlineMarket.model.MenuGroup;
import OnlineMarket.service.MenuGroupService;

@Controller
@RequestMapping("/admin/menu-group/{menuGroupId:^\\d+}/menu")
public class MenuController extends MainController{

	@Autowired
	MenuGroupService menuGroupService;

	String relativePath;

	String menuGroupPath;

	MenuGroup menuGroup;

	@ModelAttribute
	private ModelMap modelAttribute(
			@PathVariable("menuGroupId") Integer menuGroupId,
			ModelMap model) {
		menuGroupPath = "/admin/menu-group";
		relativePath = menuGroupPath+"/"+menuGroupId+"/menu";
		generateBreadcrumbs();
		breadcrumbs.add(new String[]{ "/admin", "Admin"});
		breadcrumbs.add(new String[]{ menuGroupPath, "Menu group"});
		menuGroup = menuGroupService.getByKey(menuGroupId);
		model.put("relativePath", relativePath);
		model.put("menuGroup", menuGroup);
		model.put("menuGroupPage", true);
		return model;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	private String mainPage(
			ModelMap model,
			RedirectAttributes redirectAttributes) {

		if(menuGroup == null) {
			redirectAttributes.addFlashAttribute("error", "Menu group not found");
			return "redirect:" + menuGroupPath;
		}

		model.put("pageTitle", "Menu manager for group "+menuGroup.getName());

		return "backend/menu";
	}

}
