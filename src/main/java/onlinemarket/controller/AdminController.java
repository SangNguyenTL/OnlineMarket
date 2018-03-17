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
		model.put("filterForm", new FilterForm());
		return model;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	String homePage(ModelMap model) {

		model.put("pageTitle", "Aministrator Page");
		return "backend/index";

	}

	@RequestMapping(value = { "menu" }, method = RequestMethod.GET)
	public String menuPage(ModelMap model) {

		model.put("pageTitle", "Menu");
		return "backend/index";
	}
	
	@RequestMapping("/product")
	public String productPage(
			@ModelAttribute("filterForm") FilterForm filterForm,
			ModelMap model, RedirectAttributes redirectAttributes) {
		
		model.put("pageTitle", "Product manager");
		model.put("result", productService.list(filterForm));
		model.put("path", "product");
		model.put("relativePath", "/admin/product");
		
		return "backend/product";
	}

	@RequestMapping("/product-category/{id:^\\d}/product")
	public String productPage(@PathVariable("id") Integer id, @ModelAttribute("filterForm") FilterForm filterForm,
			ModelMap model, RedirectAttributes redirectAttributes) {

		ProductCategory productCategory = productCategoryService.getByKey(id);
		if (productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}

		model.put("pageTitle", "Product manager");
		model.put("result", productService.list(filterForm));
		model.put("path", "product");
		model.put("relativePath", "/product-category/" + id + "/product");

		return "backend/product";
	}
	
	@RequestMapping(value = "/product/add", method = RequestMethod.GET)
	public String addPage(
			ModelMap model, RedirectAttributes redirectAttributes) {

		model.put("pageTitle", "Add");
		model.put("subPageTitle", "Add for new product");
		model.put("description", "Information of product");
		model.put("action", "add");
		model.put("pathAction", "/admin/product/add");
		model.put("product", new Product());
		
		return "backend/product-add";
	}
	
	@RequestMapping(value = "/product/add", method = RequestMethod.POST)
	public String processAddPage(
			@ModelAttribute("product") @Validated(value = {Default.class, AdvancedValidation.CheckSlug.class}) Product product,
			BindingResult result, ModelMap model,
			RedirectAttributes redirectAttributes) {

		if (!result.hasErrors()) {
			redirectAttributes.addFlashAttribute("success", "");
			product.setUser(currentUser);
			productService.save(product);
			return "redirect:/admin/product";
		}
		
		model.put("pageTitle", "Add");
		model.put("subPageTitle", "Add for new product");
		model.put("description", "Information of product");
		model.put("action", "add");
		model.put("pathAction", "/admin/product/add");
		model.put("product", product);
		
		return "backend/product-add";
	}
}