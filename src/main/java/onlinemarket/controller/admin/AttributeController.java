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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Attribute;
import onlinemarket.model.AttributeGroup;
import onlinemarket.model.ProductCategory;
import onlinemarket.service.AttributeGroupService;
import onlinemarket.service.AttributeService;
import onlinemarket.service.ProductCategoryService;

@Controller
@RequestMapping("/admin/product-category/{id:^\\d++}/attribute-group/${attrGroupId:^\\d+}/attribute")
public class AttributeController {

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
		
		return model;
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String mainPage(@PathVariable("id") Integer id,
			@PathVariable("attrGroupId") Integer attrGroupId,
			@ModelAttribute("filterForm") FilterForm filterForm,
			ModelMap model, RedirectAttributes redirectAttributes){
		
		filterForm.setOrderBy("priority");
		filterForm.setOrder("asc");
		
		ProductCategory productCategory = productCategoryService.getByKey(id);
		if(productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}
		
		AttributeGroup attributeGroup = attributeGroupService.getByKey(attrGroupId);
		if(attributeGroup == null) {
			redirectAttributes.addFlashAttribute("error", "Attribute group not found.");
			return "redirect:/admin/product-category/"+id+"/attribute-group";
		}
		
		model.put("pageTitle", "Attribute Manager for"+attributeGroup.getName());
		model.put("path", "product-category");
		model.put("result", attributeService.listByAttributeGroup(attributeGroup, filterForm));
		model.put("filterForm", filterForm);
		
		return "backend/attribute";
	}
	
	@RequestMapping(value = "/page/{page:^\\d+}", method = RequestMethod.GET)
	public String mainPagePagination(@PathVariable("id") Integer id,
			@PathVariable("attrGroupId") Integer attrGroupId,
			@PathVariable("page") Integer page,
			@ModelAttribute("filterForm") FilterForm filterForm,
			ModelMap model, RedirectAttributes redirectAttributes){
		
		filterForm.setCurrentPage(page);
		filterForm.setOrderBy("priority");
		filterForm.setOrder("asc");
		
		ProductCategory productCategory = productCategoryService.getByKey(id);
		if(productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}
		
		AttributeGroup attributeGroup = attributeGroupService.getByKey(attrGroupId);
		if(attributeGroup == null) {
			redirectAttributes.addFlashAttribute("error", "Attribute group not found.");
			return "redirect:/admin/product-category/"+id+"/attribute-group";
		}
		
		model.put("pageTitle", "Attribute manager for"+attributeGroup.getName());
		model.put("path", "product-category");
		model.put("result", attributeService.listByAttributeGroup(attributeGroup, filterForm));
		model.put("filterForm", filterForm);
		
		return "backend/attribute";
		
	}
	
	@RequestMapping(value = "/update/{idAttr:^\\d+}", method = RequestMethod.GET)
	public String updatePage(
			@PathVariable("id") Integer id,
			@PathVariable("attrGroupId") int attrGroupId,
			@PathVariable("attrId") int attrId, 
			ModelMap model,
			RedirectAttributes redirectAttributes) {

		ProductCategory productCategory = productCategoryService.getByKey(id);
		if(productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}
		
		AttributeGroup attributeGroup = attributeGroupService.getByKey(attrGroupId);
		if (attributeGroup == null) {
			redirectAttributes.addFlashAttribute("error", "Attribute group not found.");
			return "redirect:/admin/product-category/"+id+"/attribute-group";
		}
		
		Attribute attribute = attributeService.getByKey(attrId);
		if (attribute == null) {
			redirectAttributes.addFlashAttribute("error", "Attribute not found.");
			return "redirect:/admin/product-category/"+id+"/attribute-group/"+attrGroupId+"/attribute";
		}
		
		model.put("subPageTitle", "Update for"+ attributeGroup.getName());
		model.put("description", "Update attribute group for product category");
		model.put("pageTitle", "Update attribute group");
		model.put("provincePage", true);
		model.put("path", "product-category");
		model.put("action", "update");
		model.put("pathAction", "/admin/product-category/"+id+"/attribute-group/"+attrGroupId+"/attribute/update/"+attrId);
		model.put("attribute", attribute);
		
		return "backend/attribute-add";
		
	}
	
	@RequestMapping(value = "/update/{idAttr:^\\d+}", method = RequestMethod.POST)
	public String processUpdatePage(
			@PathVariable("id") Integer id,
			@PathVariable("attrGroupId") int attrGroupId,
			@PathVariable("attrId") int attrId,
			@Valid @ModelAttribute("attribute") Attribute attribute,
			BindingResult result,
			ModelMap model,
			RedirectAttributes redirectAttributes) {

		ProductCategory productCategory = productCategoryService.getByKey(id);
		if(productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}
		
		AttributeGroup attributeGroup = attributeGroupService.getByKey(attrGroupId);
		if (attributeGroup == null) {
			redirectAttributes.addFlashAttribute("error", "Attribute group not found.");
			return "redirect:/admin/product-category/"+id+"/attribute-group";
		}
		
		Attribute attributeCheck = attributeService.getByKey(attrId);
		if (attributeCheck == null) {
			redirectAttributes.addFlashAttribute("error", "Attribute not found.");
			return "redirect:/admin/product-category/"+id+"/attribute-group/"+attrGroupId+"/attribute";
		}
		
		if (!result.hasErrors()) {
			attributeService.update(attribute);
			redirectAttributes.addFlashAttribute("success", "");
			return "redirect:/admin/product-category/"+id+"/attribute-group/"+attrGroupId+"/attribute";
		}
		
		model.put("subPageTitle", "Update for"+ attributeGroup.getName());
		model.put("description", "Update attribute group for product category");
		model.put("pageTitle", "Update attribute group");
		model.put("provincePage", true);
		model.put("path", "product-category");
		model.put("action", "update");
		model.put("pathAction", "/admin/product-category/"+id+"/attribute-group/"+attrGroupId+"/attribute/update/"+attrId);
		model.put("attribute", attribute);
		
		return "backend/attribute-add";
		
	}
}
