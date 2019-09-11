package OnlineMarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import OnlineMarket.service.OrderService;

@Controller
@RequestMapping("/admin")
public class AdminController extends MainController {

	@Autowired
	OrderService orderService;

	@Override
	public void addMeta(ModelMap model) {
        model.put("countUser", userService.countUser());
        model.put("countBrand", brandService.count());
        model.put("countOrder", orderService.count());
        model.put("countProductCategory",productCategoryService.count());
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