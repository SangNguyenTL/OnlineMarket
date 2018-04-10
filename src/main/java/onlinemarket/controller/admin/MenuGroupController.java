package onlinemarket.controller.admin;

import javax.validation.Valid;

import onlinemarket.util.exception.menuGroup.MenuGroupNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import onlinemarket.controller.MainController;
import onlinemarket.model.MenuGroup;
import onlinemarket.service.MenuGroupService;

@Controller
@RequestMapping("/admin/menu-group")
public class MenuGroupController extends MainController {

	@Autowired
	MenuGroupService menuGroupService;

	String relativePath;

	@ModelAttribute
	public ModelMap populateAttribute(ModelMap model) {

		relativePath = "/admin/menu-group";
		generateBreadcrumbs();
		breadcrumbs.add(new String[]{"/admin", "Admin"});
		breadcrumbs.add(new String[]{relativePath, "Menu group"});
		model.put("path", "menu-group");
		model.put("pageTitle", "Menu group manager");
		model.put("relativePath", relativePath);
		model.put("menuGroupPage", true);
		model.put("pathAdd", relativePath + "/add");

		return model;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String mainPage(ModelMap model) {
		model.put("list", menuGroupService.list());
		return "backend/menu-group";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addPage(ModelMap model) {

		model.put("subPageTitle", "Add new menu group");
		model.put("description", "Information fo menu group");
		model.put("pageTitle", "Add new menu group");
		model.put("action", "add");
		model.put("path", "menu-group-add");
		model.put("pathAction", relativePath + "/add");
		model.put("menuGroup", new MenuGroup());

		return "backend/menu-group-add";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String processAddPage(@ModelAttribute("menuGroup") @Valid MenuGroup menuGroup,
			BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {

		if (!result.hasErrors()) {
			redirectAttributes.addFlashAttribute("success", "");
			menuGroupService.save(menuGroup);
			return "redirect:" + relativePath;
		}

		model.put("subPageTitle", "Add new menu group");
		model.put("description", "Information fo menu group");
		model.put("pageTitle", "Add new menu group");
		model.put("action", "add");
		model.put("path", "menu-group-add");
		model.put("pathAction", relativePath + "/add");
		model.put("menuGroup", menuGroup);

		return "backend/menu-group-add";
	}

	@RequestMapping(value = "/update/{idMenuGroup:^\\d+}", method = RequestMethod.GET)
	public String updatePage(@PathVariable("idMenuGroup") int idMenuGroup, ModelMap model,
			RedirectAttributes redirectAttributes) {

		try {
			MenuGroup menuGroup = menuGroupService.getByKey(idMenuGroup);
			if (menuGroup == null) throw new MenuGroupNotFoundException();

			model.put("subPageTitle", "Update menu group");
			model.put("description", "Information fo menu group");
			model.put("pageTitle", "Update menu group");
			model.put("action", "add");
			model.put("path", "menu-group-add");
			model.put("pathAction", relativePath + "/update" );
			model.put("menuGroup", menuGroup);

			return "backend/menu-group-add";
		}catch (MenuGroupNotFoundException e){
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:" + relativePath;
		}

	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String processUpdatePage(@ModelAttribute("menuGroup") @Valid MenuGroup menuGroup, BindingResult result,
			ModelMap model, RedirectAttributes redirectAttributes) {

		try {
			if (!result.hasErrors()) {
				redirectAttributes.addFlashAttribute("success", "");
				menuGroupService.update(menuGroup);
				return "redirect:" + relativePath;
			}

			model.put("subPageTitle", "Update menu group");
			model.put("description", "Information fo menu group");
			model.put("pageTitle", "Update menu group");
			model.put("action", "add");
			model.put("path", "menu-group-add");
			model.put("pathAction", relativePath + "/update" );
			model.put("menuGroup", menuGroup);

			return "backend/menu-group-add";
		}catch (MenuGroupNotFoundException e){
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:" + relativePath;
		}

	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public String processDeleteProvince(@RequestParam(value = "id") Integer idMenuGroup,
			RedirectAttributes redirectAttributes) {

		try {
			menuGroupService.delete(idMenuGroup);
		} catch (MenuGroupNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", "Menu group not found.");
		}

		return "redirect:" + relativePath;
	}
}
