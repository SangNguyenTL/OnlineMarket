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
import onlinemarket.model.Attribute;
import onlinemarket.model.AttributeGroup;
import onlinemarket.model.ProductCategory;
import onlinemarket.service.AttributeGroupService;
import onlinemarket.service.AttributeService;
import onlinemarket.service.ProductCategoryService;

@Controller
@RequestMapping("/admin/product-category/{id:^\\d++}/attribute-group")
public class AttributeGroupController extends MainController{

	@Autowired
	AttributeGroupService attributeGroupService;
	
	@Autowired
	AttributeService attributeService;
	
	@Autowired
	ProductCategoryService productCategoryService;
	
	@ModelAttribute
	public ModelMap populateAttribute(ModelMap model) {
		
		model.put("productCategoryPage", true);
		model.put("filterForm", new FilterForm());
		model.put("productCategory", null);
		
		return model;
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String mainPage(@PathVariable("id") Integer id,
			@ModelAttribute("filterForm") FilterForm filterForm,
			ModelMap model, RedirectAttributes redirectAttributes){
		
		ProductCategory productCategory = productCategoryService.getByKey(id);
		if(productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}
		
		model.put("pageTitle", "Province Manager");
		model.put("path", "province");
		model.put("result", attributeGroupService.listByProductCategory(productCategory, filterForm));
		model.put("filterForm", filterForm);
		
		return "backend/attribute-group";
	}
	
	
	@RequestMapping(value = "/page/{page:^\\d+}", method = RequestMethod.GET)
	public String mainPagePagination(@PathVariable("id") Integer id,
			@PathVariable("page") Integer page,
			@ModelAttribute("filterForm") FilterForm filterForm,
			ModelMap model, RedirectAttributes redirectAttributes){
		
		filterForm.setCurrentPage(page);
		
		ProductCategory productCategory = productCategoryService.getByKey(id);
		if(productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}
		
		model.put("pageTitle", "Province Manager");
		model.put("path", "province");
		model.put("result", attributeGroupService.listByProductCategory(productCategory, filterForm));
		model.put("filterForm", filterForm);
		
		return "backend/attribute-group";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addPage(@PathVariable("id") Integer id,
			ModelMap model, RedirectAttributes redirectAttributes) {
		
		ProductCategory productCategory = productCategoryService.getByKey(id);
		if(productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}
		
		model.put("subPageTitle", "Add");
		model.put("description", "Add attribute group for product category");
		model.put("pageTitle", "Add new attribute group");
		model.put("provincePage", true);
		model.put("path", "product-category");
		model.put("action", "add");
		model.put("pathAction", "/admin/product-category/"+id+"/attribute-group/add");
		model.put("attributeGroup", new AttributeGroup());
		
		return "backend/attribute-group-add";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String processAddPage(
			@PathVariable("id") Integer id,
			@ModelAttribute("attributeGroup") @Valid AttributeGroup attributeGroup,
			BindingResult result, ModelMap model, 
			RedirectAttributes redirectAttributes) {
		
		ProductCategory productCategory = productCategoryService.getByKey(id);
		if(productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}
		
		if (!result.hasErrors()) {
			redirectAttributes.addAttribute("success", "");
			attributeGroupService.save(attributeGroup);
			return "redirect:/admin/brand";
		}
		model.put("subPageTitle", "Add");
		model.put("description", "Add attribute group for product category");
		model.put("pageTitle", "Add new attribute group");
		model.put("provincePage", true);
		model.put("path", "product-category");
		model.put("action", "add");
		model.put("pathAction", "/admin/product-category/"+id+"/attribute-group/add");
		model.put("attributeGroup", attributeGroup);
		
		return "backend/attribute-group-add";
	}
	
	@RequestMapping(value = "/update/{idAttr:^\\d+}", method = RequestMethod.GET)
	public String updatePage(
			@PathVariable("id") Integer id,
			@PathVariable("idAttr") int idAttr, 
			ModelMap model,
			RedirectAttributes redirectAttributes) {

		ProductCategory productCategory = productCategoryService.getByKey(id);
		if(productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}
		
		AttributeGroup attributeGroup = attributeGroupService.getByKey(idAttr);
		if (attributeGroup == null) {
			redirectAttributes.addFlashAttribute("error", "Attribute group not found.");
			return "redirect:/admin/product-category/"+id+"/attribute-group";
		}
		
		model.put("subPageTitle", "Update");
		model.put("description", "Update attribute group for product category");
		model.put("pageTitle", "Update attribute group");
		model.put("provincePage", true);
		model.put("path", "product-category");
		model.put("action", "update");
		model.put("pathAction", "/admin/product-category/"+id+"/attribute-group/update/"+idAttr);
		model.put("attributeGroup", attributeGroup);
		
		return "backend/attribute-group-add";
		
	}

	
	@RequestMapping(value = "/update/{id:^\\d+}", method = RequestMethod.POST)
	public String processUpdatePage(
			@PathVariable("id") Integer id,
			@PathVariable("idAttr") int idAttr,
			@ModelAttribute("attributeGroup") @Valid AttributeGroup attributeGroup,
			BindingResult result,
			ModelMap model,
			RedirectAttributes redirectAttributes) {
		
			ProductCategory productCategory = productCategoryService.getByKey(id);
			if(productCategory == null) {
				redirectAttributes.addFlashAttribute("error", "Product category not found.");
				return "redirect:/admin/product-category";
			}
			
			AttributeGroup attributeGroupCheck = attributeGroupService.getByKey(idAttr);
			if (attributeGroupCheck == null) {
				redirectAttributes.addFlashAttribute("error", "Attribute group not found.");
				return "redirect:/admin/product-category/"+id+"/attribute-group";
			}
			
			if (!result.hasErrors()) {
				attributeGroupService.update(attributeGroup);
				redirectAttributes.addFlashAttribute("success", "");
				return "redirect:/admin/product-category/"+id+"/attribute-group";
			}
			
			model.put("subPageTitle", "Update");
			model.put("description", "Update attribute group for product category");
			model.put("pageTitle", "Update attribute group");
			model.put("provincePage", true);
			model.put("path", "product-category");
			model.put("action", "update");
			model.put("pathAction", "/admin/product-category/"+id+"/attribute-group/update/"+idAttr);
			model.put("attributeGroup", attributeGroup);
			
			return "backend/attribute-group-add";
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public String processDeleteProvince(
			@PathVariable("id") Integer id,
			@RequestParam(value = "id", required = true) Integer idAttr,
			RedirectAttributes redirectAttributes) {
			
		if(id == null) {
			redirectAttributes.addAttribute("error", "Program isn't get province id!");
			return "redirect:/admin/province";
		}
		
		AttributeGroup attributeGroup = attributeGroupService.getByKey(idAttr);
		if (attributeGroup == null) {
			redirectAttributes.addAttribute("error", "The attribur group isn't exist!");
		}else {
			Attribute attribute = attributeService.getByAttributeGroup(attributeGroup);
			if(attribute != null)
				redirectAttributes.addAttribute("error", "The attribur group has already had attribute!");
			else {
				redirectAttributes.addAttribute("success", "");
				attributeGroupService.delete(attributeGroup);
			}
		}
		return "redirect:/admin/product-category/"+id+"/attribute-group/";
	}
}
