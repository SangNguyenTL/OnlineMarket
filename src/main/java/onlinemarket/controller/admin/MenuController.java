package onlinemarket.controller.admin;

import java.util.List;

import javax.validation.Valid;

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
import onlinemarket.model.Menu;
import onlinemarket.model.MenuGroup;
import onlinemarket.service.MenuGroupService;
import onlinemarket.service.MenuService;

@Controller
@RequestMapping("/admin/menu-group/{menuGroupId:^\\d+}/menu")
public class MenuController extends MainController{

	@Autowired
	MenuGroupService menuGroupService;
	
	@Autowired
	MenuService menuService;
	
	String relativePath;
	
	MenuGroup menuGroup;
	
	List<MenuGroup> menuGroupList;
	
	@ModelAttribute
	private ModelMap populateAttribute(
			@PathVariable("menuGroupId") Integer menuGrid,
			ModelMap model) {
		
		relativePath = "/admin/menu-group"+menuGrid+"/menu";
		menuGroup = menuGroupService.getByKey(menuGrid);
		menuGroupList = menuGroupService.list();
		model.put("menuGroupList", menuGroupList);
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
			return "redirect:/admin/menu-group";
		}
		
		model.put("pageTitle", "Menu manager for group "+menuGroup.getName());
		model.put("list", menuService.listByDeclaration("menugGroup", menuGroup));
		
		return "backend/menu";
	}
	

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addPage(ModelMap model) {

		model.put("subPageTitle", "Add new menu group");
		model.put("description", "Information fo menu group");
		model.put("pageTitle", "Add new menu group");
		model.put("action", "add");
		model.put("pathAction", relativePath + "/add");
		model.put("menuGroupList", menuGroupList);
		model.put("menu", new Menu());

		return "backend/menu-add";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String processAddPage(@ModelAttribute("menu") @Valid Menu menu,
			BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {

		if (!result.hasErrors()) {
			redirectAttributes.addFlashAttribute("success", "");
			menuGroupService.save(menuGroup);
			return "redirect:/admin/menu-group";
		}

		model.put("subPageTitle", "Add new menu group");
		model.put("description", "Information fo menu group");
		model.put("pageTitle", "Add new menu group");
		model.put("action", "add");
		model.put("pathAction", relativePath + "/add");
		model.put("menu", menu);

		return "backend/menu-add";
	}

	@RequestMapping(value = "/update/{menuId:^\\d+}", method = RequestMethod.GET)
	public String updatePage(@PathVariable("menuId") int menuId, ModelMap model,
			RedirectAttributes redirectAttributes) {

		if (menuGroup == null) {
			redirectAttributes.addFlashAttribute("error", "Menu group not found.");
			return "redirect:/admin/menu-group";
		}
		
		Menu menu = menuService.getByKey(menuId);
		if (menu == null) {
			redirectAttributes.addFlashAttribute("error", "Menu not found.");
			return "redirect:" + relativePath;
		}

		model.put("subPageTitle", "Update menu");
		model.put("description", "Information fo menu");
		model.put("pageTitle", "Update menu");
		model.put("action", "add");
		model.put("pathAction", relativePath + "/update/" + menuId);
		model.put("menu", menu);

		return "backend/menu-add";

	}

	@RequestMapping(value = "/update/{menuId:^\\d+}", method = RequestMethod.POST)
	public String processUpdatePage(@PathVariable("menuId") int menuId,
			@ModelAttribute("menu") @Valid Menu menu, BindingResult result,
			ModelMap model, RedirectAttributes redirectAttributes) {

		if (menuGroup == null) {
			redirectAttributes.addFlashAttribute("error", "Menu group not found.");
			return "redirect:/admin/menu-group";
		}
		
		Menu menuCheck = menuService.getByKey(menuId);
		if (menuCheck == null) {
			redirectAttributes.addFlashAttribute("error", "Menu not found.");
			return "redirect:" + relativePath;
		}
		
		if (!result.hasErrors()) {
			redirectAttributes.addFlashAttribute("success", "");
//			menuService.update(menu);
			return "redirect:" + relativePath;
		}

		model.put("subPageTitle", "Update menu");
		model.put("description", "Information fo menu");
		model.put("pageTitle", "Update menu");
		model.put("action", "add");
		model.put("pathAction", relativePath + "/update/" + menuId);
		model.put("menu", menu);
		
		return "backend/menu-add";
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public String processDeleteProvince(@RequestParam(value = "id", required = true) Integer idMenuGroup,
			RedirectAttributes redirectAttributes) {

		if (idMenuGroup == null) {
			redirectAttributes.addAttribute("error", "Program wasn't gotten attibute group id!");
			return "redirect:" + relativePath;
		}
		
		MenuGroup menuGroupCheck = menuGroupService.getByKey(idMenuGroup);
		if (menuGroupCheck == null) {
			redirectAttributes.addFlashAttribute("error", "Menu group not found.");
			return "redirect:/admin/menu-group";
		}
		
		menuGroupService.delete(menuGroupCheck);
		
		return "redirect:" + relativePath;
	}
}
