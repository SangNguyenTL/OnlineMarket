package onlinemarket.controller.admin;

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
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Product;
import onlinemarket.model.ProductCategory;
import onlinemarket.service.AttributeGroupService;
import onlinemarket.service.ProductCategoryService;
import onlinemarket.service.ProductService;

@Controller
@RequestMapping("/admin/product-category/{id:^\\d}/product")
public class ProductController extends MainController{


	@Autowired
	ProductService productService;
	
	@Autowired
	ProductCategoryService productCategoryService;
	
	@Autowired
	AttributeGroupService attributeGroupService;
	
	ProductCategory productCategory;
	
	String relativePath;
	
	FilterForm filterForm;
	
	@ModelAttribute
	public ModelMap populateAttribute(
			@PathVariable("id") Integer id,
			ModelMap model) {
		relativePath = buildRelativePath(id);
		productCategory = productCategoryService.getByKey(id);
		filterForm = new FilterForm();
		
		model.put("productPage", true);
		model.put("path", productCategory.getSlug()+"-add");
		model.put("productCategory", productCategory);
		model.put("filterForm", filterForm);
		model.put("relativePath", relativePath);
		model.put("pathAdd", relativePath+"/add");
		
		return model;
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String mainPage(ModelMap model, RedirectAttributes redirectAttributes){
		
		if(productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}
		
		model.put("pageTitle", "Product manager for "+ productCategory.getName());
		model.put("result", productService.listByProductCategory(productCategory, filterForm));
		model.put("filterForm", filterForm);
		
		return "backend/product";
	}
	
	
	@RequestMapping(value = "/page/{page:^\\d+}", method = RequestMethod.GET)
	public String mainPagePagination(
			@PathVariable("page") Integer page,
			ModelMap model, RedirectAttributes redirectAttributes){
		
		filterForm.setCurrentPage(page);
		filterForm.setOrderBy("priority");
		filterForm.setOrder("asc");
		
		if(productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}
		
		model.put("pageTitle", "Product manager for "+ productCategory.getName());
		model.put("result", productService.listByProductCategory(productCategory, filterForm));
		model.put("filterForm", filterForm);
		
		return "backend/product";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addPage(
			ModelMap model, RedirectAttributes redirectAttributes) {
		
		if(productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}
		
		model.put("pageTitle", "Add new product");
		model.put("listAttributeGroup", attributeGroupService.listWithAttributeByProductCategory(productCategory));
		model.put("subPageTitle", "Add for "+ productCategory.getName());
		model.put("description", "Add product for "+ productCategory.getName());
		model.put("action", "add");
		model.put("pathAction", relativePath+"/add");
		model.put("product", new Product());
		
		return "backend/product-add";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String processAddPage(
			@ModelAttribute("product") @Valid Product product,
			BindingResult result, ModelMap model,
			RedirectAttributes redirectAttributes) {
		
		if(productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}
		
		if (!result.hasErrors()) {
			product.setProductCategory(productCategory);
			redirectAttributes.addFlashAttribute("success", "");
			productService.save(product);
			return "redirect:"+relativePath;
		}
		
		model.put("pageTitle", "Add new product");
		model.put("subPageTitle", "Add for "+ productCategory.getName());
		model.put("description", "Add product for "+ productCategory.getName());
		model.put("action", "add");
		model.put("pathAction", relativePath+"/add");
		model.put("product", product);
		
		return "backend/product-add";
	}
	
	@RequestMapping(value = "/update/{idPr:^\\d+}", method = RequestMethod.GET)
	public String updatePage(
			@PathVariable("idPr") int idPr, 
			ModelMap model, RedirectAttributes redirectAttributes) {

		if(productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}
		
		Product product = productService.getByKey(idPr);

		if(product == null) {
			redirectAttributes.addFlashAttribute("error", "Product not found.");
			return "redirect:"+relativePath;
		}
		
		model.put("pageTitle", "Update new product");
		model.put("subPageTitle", "Update for "+ productCategory.getName());
		model.put("description", "Update product for "+ productCategory.getName());
		model.put("action", "update");
		model.put("pathAction", relativePath+"/update");
		model.put("product", product);
		
		return "backend/product-add";
		
	}

	
	@RequestMapping(value = "/update/{idPr:^\\d+}", method = RequestMethod.POST)
	public String processUpdatePage(
			@PathVariable("idPr") int idPr,
			@ModelAttribute("product") @Valid Product product,
			BindingResult result, ModelMap model,
			RedirectAttributes redirectAttributes) {
			
			if(productCategory == null) {
				redirectAttributes.addFlashAttribute("error", "Product category not found.");
				return "redirect:/admin/product-category";
			}
			
			Product productCheck = productService.getByKey(idPr);
			if(productCheck == null) {
				redirectAttributes.addFlashAttribute("error", "Product not found.");
				return "redirect:"+relativePath;
			}
			
			if(!result.hasErrors()) {
				product.setProductCategory(productCategory);
				productService.update(product);
				redirectAttributes.addFlashAttribute("success", "");
				return "redirect:"+relativePath;
			}
			
			model.put("pageTitle", "Update new product");
			model.put("subPageTitle", "Update for "+ productCategory.getName());
			model.put("description", "Update product for "+ productCategory.getName());
			model.put("action", "update");
			model.put("pathAction", relativePath+"/update");
			model.put("product", product);
			
			return "backend/product-add";
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public String processDeleteProvince(
			@RequestParam(value = "id", required = true) Integer idAttr,
			RedirectAttributes redirectAttributes) {
			
		
		return "redirect:"+relativePath;
	}
	
	public String buildRelativePath(int id) {
		return "/admin/product-category/"+id+"/attribute-group";
	}
	
}
