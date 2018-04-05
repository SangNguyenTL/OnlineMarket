package onlinemarket.controller;

import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Product;
import onlinemarket.model.ProductCategory;
import onlinemarket.model.other.AdvancedValidation;
import onlinemarket.service.ProductCategoryService;
import onlinemarket.service.ProductService;

@Controller
@RequestMapping("/admin")
public class AdminController extends MainController {

	@Autowired
	ProductCategoryService productCategoryService;

	@Autowired
	ProductService productService;

	@ModelAttribute
	public ModelMap populateAttribute(ModelMap model) {
		return model;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	String mainPage(ModelMap model) {

		model.put("pageTitle", "Administrator Page");
		return "backend/index";

	}

	@RequestMapping(value = { "menu" }, method = RequestMethod.GET)
	public String menuPage(ModelMap model) {

		model.put("pageTitle", "Menu");
		return "backend/index";
	}

}