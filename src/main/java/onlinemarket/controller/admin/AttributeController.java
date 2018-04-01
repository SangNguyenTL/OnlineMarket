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
@RequestMapping("/admin/product-category/{id:^\\d++}/attribute-group/{attrGroupId:^\\d+}/attribute")
public class AttributeController extends MainController {

	@Autowired
	AttributeGroupService attributeGroupService;

	@Autowired
	AttributeService attributeService;

	@Autowired
	ProductCategoryService productCategoryService;

	ProductCategory productCategory;
	
	AttributeGroup attributeGroup;
	
	String relativePathNonAttriId;
	
	FilterForm filterForm;
	
	@ModelAttribute
	public ModelMap populateAttribute(@PathVariable("id") Integer id,
			@PathVariable("attrGroupId") Integer attrGroupId,
			ModelMap model) {

		relativePathNonAttriId = buildRelativePath(id);
		relativePath = buildRelativePath(id, attrGroupId);
		productCategory = productCategoryService.getByKey(id);
		attributeGroup = attributeGroupService.getByKey(attrGroupId);
		filterForm = new FilterForm();
		
		model.put("productCategoryPage", true);
		model.put("filterForm", filterForm);
		model.put("productCategory", productCategory);
		model.put("attributeGroup", attributeGroup);
		model.put("path", "product-category");
		model.put("relativePath", relativePath);
		model.put("pathAdd", relativePath + "/add");

		return model;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String mainPage(
			ModelMap model,
			RedirectAttributes redirectAttributes) {

		filterForm.setOrderBy("priority");
		filterForm.setOrder("asc");

		if (productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}

		if (attributeGroup == null) {
			redirectAttributes.addFlashAttribute("error", "Attribute group not found.");
			return "redirect:" + relativePathNonAttriId;
		}

		model.put("pageTitle", "Attribute Manager for " + attributeGroup.getName());
		model.put("result", attributeService.listByAttributeGroup(attributeGroup, filterForm));
		model.put("filterForm", filterForm);

		return "backend/attribute";
	}

	@RequestMapping(value = "/page/{page:^\\d+}", method = RequestMethod.GET)
	public String mainPagePagination(
			@PathVariable("page") Integer page, ModelMap model,
			RedirectAttributes redirectAttributes) {

		filterForm.setCurrentPage(page);
		filterForm.setOrderBy("priority");
		filterForm.setOrder("asc");

		if (productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}

		if (attributeGroup == null) {
			redirectAttributes.addFlashAttribute("error", "Attribute group not found.");
			return "redirect:" + relativePathNonAttriId;
		}

		model.put("pageTitle", "Attribute manager for " + attributeGroup.getName());
		model.put("result", attributeService.listByAttributeGroup(attributeGroup, filterForm));
		model.put("filterForm", filterForm);

		return "backend/attribute";

	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addPage(ModelMap model, RedirectAttributes redirectAttributes) {

		if (productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}

		if (attributeGroup == null) {
			redirectAttributes.addFlashAttribute("error", "Attribute group not found.");
			return "redirect:" + relativePathNonAttriId;
		}

		model.put("subPageTitle", "Add");
		model.put("description", "Add attribute for " + attributeGroup.getName());
		model.put("pageTitle", "Add new attribute");
		model.put("action", "add");
		model.put("pathAction", relativePath + "/add");
		model.put("attribute", new Attribute());

		return "backend/attribute-add";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String processAddPage(
			@ModelAttribute("attribute") @Valid Attribute attribute, BindingResult result, ModelMap model,
			RedirectAttributes redirectAttributes) {

		if (productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}

		if (attributeGroup == null) {
			redirectAttributes.addFlashAttribute("error", "Attribute group not found.");
			return "redirect:" + relativePathNonAttriId;
		}

		if (!result.hasErrors()) {
			attribute.setAttributeGroup(attributeGroup);
			redirectAttributes.addFlashAttribute("success", "");
			attributeService.save(attribute);
			return "redirect:" + relativePath;
		}

		model.put("subPageTitle", "Add");
		model.put("description", "Add attribute for " + attributeGroup.getName());
		model.put("pageTitle", "Add new attribute");
		model.put("action", "add");
		model.put("pathAction", relativePath + "/add");
		model.put("attribute", attribute);

		return "backend/attribute-add";
	}

	@RequestMapping(value = "/update/{attrId:^\\d+}", method = RequestMethod.GET)
	public String updatePage(
			@PathVariable("attrId") int attrId, ModelMap model, RedirectAttributes redirectAttributes) {

		if (productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}

		if (attributeGroup == null) {
			redirectAttributes.addFlashAttribute("error", "Attribute group not found.");
			return "redirect:" + relativePathNonAttriId;
		}

		Attribute attribute = attributeService.getByKey(attrId);
		if (attribute == null) {
			redirectAttributes.addFlashAttribute("error", "Attribute not found.");
			return "redirect:" + relativePath;
		}

		model.put("subPageTitle", "Update for " + attributeGroup.getName());
		model.put("description", "Update attribute for" + attributeGroup.getName());
		model.put("pageTitle", "Update attribute group");
		model.put("action", "update");
		model.put("pathAction", relativePath + "/update/" + attrId);
		model.put("attribute", attribute);

		return "backend/attribute-add";

	}

	@RequestMapping(value = "/update/{attrId:^\\d+}", method = RequestMethod.POST)
	public String processUpdatePage(
			@PathVariable("attrId") int attrId, @Valid @ModelAttribute("attribute") Attribute attribute,
			BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {

		if (productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}

		if (attributeGroup == null) {
			redirectAttributes.addFlashAttribute("error", "Attribute group not found.");
			return "redirect:"+relativePathNonAttriId;
		}

		Attribute attributeCheck = attributeService.getByKey(attrId);
		if (attributeCheck == null) {
			redirectAttributes.addFlashAttribute("error", "Attribute not found.");
			return "redirect:"+relativePath;
		}

		if (!result.hasErrors()) {
			attribute.setAttributeGroup(attributeGroup);
//			attributeService.update(attribute);
			redirectAttributes.addFlashAttribute("success", "");
			return "redirect:"+relativePath;
		}

		model.put("subPageTitle", "Update for " + attributeGroup.getName());
		model.put("description", "Update attribute for" + attributeGroup.getName());
		model.put("pageTitle", "Update attribute group");
		model.put("action", "update");
		model.put("pathAction", relativePath + "/update/" + attrId);
		model.put("attribute", attribute);

		return "backend/attribute-add";

	}

	protected String buildRelativePath(int categoryId) {
		return "/admin/product-category/" + categoryId + "/attribute-group";
	}

	protected String buildRelativePath(int categoryId, int attributeGroupId) {
		return "/admin/product-category/" + categoryId + "/attribute-group/" + attributeGroupId + "/attribute";
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public String processDeleteProvince(@PathVariable("id") Integer id,
			@PathVariable("attrGroupId") Integer attrGroupId,
			@RequestParam(value = "id", required = true) Integer idAttr, RedirectAttributes redirectAttributes) {

		if (attrGroupId == null) {
			redirectAttributes.addAttribute("error", "Program wasn't gotten attibute id!");
			return "redirect:/admin/province";
		}

		Attribute attribute = attributeService.getByKey(idAttr);
		if (attribute == null) {
			redirectAttributes.addAttribute("error", "The attribute isn't been exist!");
		} else {
//			ProductAttribute productAttribute = productAttributeService.getByAttribute(attribute);
//			if (productAttribute != null)
//				redirectAttributes.addAttribute("error", "The attribute has already been had product!");
//			else {
//				redirectAttributes.addFlashAttribute("success", "success");
//				attributeService.delete(attribute);
//			}
		}
		return "redirect:" + relativePath;
	}
}
