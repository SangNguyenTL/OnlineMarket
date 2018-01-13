package onlinemarket.controller.admin;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Attribute;
import onlinemarket.model.AttributeGroup;
import onlinemarket.model.Product;
import onlinemarket.model.ProductAttribute;
import onlinemarket.model.ProductAttributeId;
import onlinemarket.model.ProductCategory;
import onlinemarket.service.AttributeGroupService;
import onlinemarket.service.AttributeService;
import onlinemarket.service.ProductAttributeService;
import onlinemarket.service.ProductCategoryService;
import onlinemarket.service.ProductService;

@Controller
@RequestMapping("/admin/product-category/{categoryId:^\\d+}/product/{productId:^\\d+}/attribute-group")
public class ProductAttributeController extends MainController {

	@Autowired
	ProductService productService;

	@Autowired
	ProductCategoryService productCategoryService;

	@Autowired
	ProductAttributeService productAttributeService;

	@Autowired
	AttributeGroupService attributeGroupService;

	@Autowired
	AttributeService attributeService;

	FilterForm filterForm;

	String relativePath;

	Product product;

	ProductCategory productCategory;

	@ModelAttribute
	public ModelMap populateAttribute(@PathVariable("categoryId") Integer categoryId,
			@PathVariable("productId") Integer productId, ModelMap model) {
		productCategory = productCategoryService.getByKey(categoryId);
		product = productService.getByKey(productId);
		filterForm = new FilterForm();
		relativePath = "/admin/product-category/" + categoryId + "/product/" + productId + "/attribute-group";
		model.put("filterForm", new FilterForm());
		model.put("relativePath", relativePath);

		return model;
	}

	@RequestMapping("")
	private String mainPage(ModelMap model, RedirectAttributes redirectAttributes) {

		if (productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}

		if (product == null) {
			redirectAttributes.addFlashAttribute("error", "Product not found.");
			return "redirect:/admin/product";
		}

		model.put("pageTitle", "Attribute group of " + product.getName());
		model.put("list", attributeGroupService.listByProductCategory(productCategory));
		model.put("path", "product");

		return "backend/product-attribute-group";
	}

	@RequestMapping("/{attributeGroupId:^\\d+}/product-attribute")
	private String mainPageAttribute(@PathVariable("attributeGroupId") Integer attributeGroupId, ModelMap model,
			RedirectAttributes redirectAttributes) {

		if (productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}

		AttributeGroup attributeGroup = attributeGroupService.getByKey(attributeGroupId);
		if (attributeGroup == null) {
			redirectAttributes.addFlashAttribute("error", "Attribtue group not found.");
			return "redirect:/admin/product-category";
		}

		if (product == null) {
			redirectAttributes.addFlashAttribute("error", "Product not found.");
			return "redirect:/admin/product";
		}

		List<Attribute> listAttr = attributeService.listByAttributeGroupNoneFilter(attributeGroup);
		List<ProductAttribute> productAttributeList = new ArrayList<>();
		for (Attribute attribute : listAttr) {
			ProductAttributeId productAttributeId = new ProductAttributeId(attribute.getId(), product.getId());
			ProductAttribute productAttribute = productAttributeService.getByKey(productAttributeId);
			if (productAttribute == null) {
				attribute.setAttributeGroup(attributeGroup);
				productAttribute = new ProductAttribute(productAttributeId, attribute, product, "");
			}
			productAttributeList.add(productAttribute);
		}

		model.put("pageTitle", attributeGroup.getName() + " attributes of " + product.getName());
		model.put("list", productAttributeList);
		model.put("path", "product");

		return "backend/product-attribute";
	}

	@RequestMapping(value = "/{attributeGroupId:^\\d+}/product-attribute/update/{attributeId:^\\d+}", method = RequestMethod.GET)
	private String showAttribute(@PathVariable("attributeGroupId") Integer attributeGroupId,
			@PathVariable("attributeId") Integer attributeId, ModelMap model, RedirectAttributes redirectAttributes) {

		if (productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}

		AttributeGroup attributeGroup = attributeGroupService.getByKey(attributeGroupId);
		if (attributeGroup == null) {
			redirectAttributes.addFlashAttribute("error", "Attribtue group not found.");
			return "redirect:/admin/product-category";
		}

		if (product == null) {
			redirectAttributes.addFlashAttribute("error", "Product not found.");
			return "redirect:/admin/product";
		}

		Attribute attribute = attributeService.getByKey(attributeId);
		if (attribute == null) {
			redirectAttributes.addFlashAttribute("error", "Product not found.");
			return "redirect:" + relativePath;
		}

		ProductAttributeId productAttributeId = new ProductAttributeId(attributeId, product.getId());
		ProductAttribute productAttribute = productAttributeService.getByKey(productAttributeId);
		if (productAttribute == null)
			productAttribute = new ProductAttribute(productAttributeId, attribute, product, "");

		model.put("pageTitle", "Update information of " + attribute.getName() + " for " + product.getName());
		model.put("subPageTitle", "Update");
		model.put("productAttribute", productAttribute);
		model.put("path", "product");
		model.put("pathAction", relativePath + "/" + attributeGroupId + "/product-attribute/update/" + attributeId);

		return "backend/product-attribute-add";
	}

	@RequestMapping(value = "/{attributeGroupId:^\\d+}/product-attribute/update/{attributeId:^\\d+}", method = RequestMethod.POST)
	private String processAttribute(@PathVariable("attributeGroupId") Integer attributeGroupId,
			@PathVariable("attributeId") Integer attributeId,
			@Valid @ModelAttribute("productAttribute") ProductAttribute productAttribute, BindingResult result,
			ModelMap model, RedirectAttributes redirectAttributes) {

		if (productCategory == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/product-category";
		}

		AttributeGroup attributeGroup = attributeGroupService.getByKey(attributeGroupId);
		if (attributeGroup == null) {
			redirectAttributes.addFlashAttribute("error", "Attribtue group not found.");
			return "redirect:/admin/product-category";
		}

		if (product == null) {
			redirectAttributes.addFlashAttribute("error", "Product not found.");
			return "redirect:/admin/product";
		}

		Attribute attribute = attributeService.getByKey(attributeId);
		if (attribute == null) {
			redirectAttributes.addFlashAttribute("error", "Attribute not found.");
			return "redirect:" + relativePath;
		}

		if (!result.hasErrors()) {
			ProductAttributeId productAttributeId = new ProductAttributeId(attributeId, product.getId());
			productAttribute.setId(productAttributeId);
			productAttribute.setProduct(product);
			productAttribute.setAttribute(attribute);

			ProductAttribute productAttributeCheck = productAttributeService.getByKey(productAttributeId);
			if (productAttributeCheck == null)
				productAttributeService.save(productAttribute);
			else
				productAttributeService.update(productAttribute);
			redirectAttributes.addFlashAttribute("success", "Attribute not found.");
			return "redirect:" + relativePath+"/"+attributeGroupId+"/product-attribute";
		}

		model.put("pageTitle", "Update information of " + attribute.getName() + " for " + product.getName());
		model.put("subPageTitle", "Update");
		model.put("productAttribute", productAttribute);
		model.put("path", "product");
		model.put("pathAction", relativePath + "/" + attributeGroupId + "/product-attribute/update/" + attributeId);

		return "backend/product-attribute-add";
	}
}
