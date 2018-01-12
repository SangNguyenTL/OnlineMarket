package onlinemarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.ProductCategory;
import onlinemarket.service.ProductCategoryService;
import onlinemarket.service.ProductService;



@Controller
@RequestMapping("/admin")
public class AdminController extends MainController{
	
	@Autowired
	ProductCategoryService productCategoryService;
	
	@Autowired
	ProductService productService;
	
	@ModelAttribute
	public ModelMap populateAttribute(
			@PathVariable("id") Integer id,
			ModelMap model) {
		
		model.put("productPage", true);
		model.put("filterForm", new FilterForm());
		
		
		return model;
	}
	
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
	
	@RequestMapping("/product-category/{id:^\\d}/product")
	public String productPage(
			@PathVariable("id") Integer id,
			@ModelAttribute("filterForm") FilterForm filterForm,
			ModelMap model, RedirectAttributes redirectAttributes) {
		
		ProductCategory productCategory = productCategoryService.getByKey(id);
		if(productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}
		
		model.put("pageTitle", "Product manager");
		model.put("result", productService.list(filterForm));
		model.put("path", "product");
		model.put("relativePath", "/product-category/"+id+"/product");
		
		return "backend/product";
	}
}