package onlinemarket.controller.admin;

import javax.validation.Valid;
import javax.validation.groups.Default;

import onlinemarket.form.filter.FilterProduct;
import onlinemarket.model.other.AdvancedValidation;
import onlinemarket.util.exception.product.ProductHasCommentException;
import onlinemarket.util.exception.product.ProductNotFoundException;
import onlinemarket.util.exception.productCategory.ProductCategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
import onlinemarket.service.ProductService;

@Controller
@RequestMapping("/admin/product-category/{productCategoryId:^\\d}/product")
public class ProductByCategoryController extends MainController {

	@Autowired
	ProductService productService;

	@Autowired
	AttributeGroupService attributeGroupService;

	private ProductCategory productCategory;

	private String productCategoryPath;

	private String relativePath;

	private FilterForm filterForm;

	private FilterProduct filterProduct;

	@ModelAttribute
	public ModelMap populateAttribute(@PathVariable("productCategoryId") Integer productCategoryId, ModelMap model) {


		productCategory = productCategoryService.getByKey(productCategoryId);
		productCategoryPath = "/admin/product-category";
		relativePath = productCategoryPath + "/" + productCategoryId + "/product";

		generateBreadcrumbs();
		breadcrumbs.add(new String[]{ productCategoryPath, "Product Category"});
		breadcrumbs.add(new String[]{ relativePath, "Product"});

		filterForm = new FilterForm();
		filterProduct = new FilterProduct(filterForm);
		model.put("productPage", true);
		model.put("path", productCategory.getSlug() + "-product");
		model.put("productCategory", productCategory);
		model.put("filterForm", filterForm);
		model.put("filterProduct", filterProduct);
		model.put("relativePath", relativePath);
		model.put("pathAdd", relativePath + "/add");
		model.put("pageType", "product");

		return model;
	}

	@RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
	public String mainPage(@ModelAttribute("filterProduct") FilterProduct filterProduct, ModelMap model, RedirectAttributes redirectAttributes) {

		try{
			filterProduct.setFilterForm(filterForm);
			if(filterProduct.getState() != null){
				filterForm.getGroupSearch().put("state", filterProduct.getState());
			}

			model.put("result", productService.listByProductCategory(productCategory, filterForm));
			model.put("pageTitle", "Product manager for " + productCategory.getName());
			model.put("filterProduct", filterProduct);
			model.put("filterForm", filterForm);

			return "backend/product";
		} catch (ProductCategoryNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:" + productCategoryPath;
		}

	}

	@RequestMapping(value = "/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
	public String mainPagePagination(@ModelAttribute("filterProduct") FilterProduct filterProduct, @PathVariable("page") Integer page, ModelMap model,
			RedirectAttributes redirectAttributes) {

		try{
			filterForm.setCurrentPage(page);
			if(filterProduct.getState() != null){
				filterForm.getGroupSearch().put("state", filterProduct.getState());
			}

			model.put("result", productService.listByProductCategory(productCategory, filterForm));
			model.put("pageTitle", "Product manager for " + productCategory.getName());
			model.put("filterProduct", filterProduct);
			model.put("filterForm", filterForm);

			return "backend/product";
		} catch (ProductCategoryNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:" + productCategoryPath;
		}
	}

	@RequestMapping( value = "/add", method = RequestMethod.GET)
	public String addPage(ModelMap modelMap, RedirectAttributes redirectAttributes){

		 try {
			 if(productCategory == null) throw new ProductCategoryNotFoundException();

			 modelMap.put("subPageTitle", "Add");
			 modelMap.put("description", "Add product for " + productCategory.getName());
			 modelMap.put("pageTitle", "Add new product");
			 modelMap.put("action", "add");
			 modelMap.put("pathAction", relativePath + "/add");
			 modelMap.put("product", new Product());

		} catch (ProductCategoryNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:" + productCategoryPath;
		}

		return "backend/product-add";
	}

	@RequestMapping( value = "/add", method = RequestMethod.POST)
	public String processAddPage(@Validated(value = {AdvancedValidation.CheckSlug.class, Default.class}) @ModelAttribute("product") Product product, ModelMap modelMap, RedirectAttributes redirectAttributes, BindingResult result){
		try{

			if(!result.hasErrors()){
				productService.save(product, productCategory, currentUser);
				redirectAttributes.addFlashAttribute("success", true);
			}else{
				modelMap.put("subPageTitle", "Add");
				modelMap.put("description", "Add product for " + productCategory.getName());
				modelMap.put("pageTitle", "Add new product");
				modelMap.put("action", "add");
				modelMap.put("pathAction", relativePath + "/add");
				modelMap.put("product", product);

				return "backend/product-add";
			}

		}catch (ProductCategoryNotFoundException e){
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}

		return "redirect:" + relativePath;
	}

	@RequestMapping(value = "/update/{productId:^\\d+}", method = RequestMethod.GET)
	public String updatePage(@PathVariable("productId") Integer productId, ModelMap modelMap, RedirectAttributes redirectAttributes) {

		try {
			if(productCategory == null) throw new ProductCategoryNotFoundException();
			Product product = productService.getByKey(productId);
			if(product == null) throw new ProductNotFoundException();

			modelMap.put("subPageTitle", "Update new product");
			modelMap.put("description", "Update product for " + productCategory.getName());
			modelMap.put("pageTitle", "Update new product");
			modelMap.put("action", "update");
			modelMap.put("pathAction", relativePath + "/update");
			modelMap.put("product", product);

		} catch (ProductCategoryNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:" + productCategoryPath;
		} catch (ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:" + relativePath;
		}

		return "backend/product-add";

	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String processUpdatePage(@ModelAttribute("product") Product product, ModelMap modelMap,BindingResult result,RedirectAttributes redirectAttributes) {

		try {
			if(!result.hasErrors()){
				productService.update(product, productCategory, currentUser);
				redirectAttributes.addFlashAttribute("success", true);
				return "redirect:"+ relativePath;
			}else{
				modelMap.put("subPageTitle", "Update new product");
				modelMap.put("description", "Update product for " + productCategory.getName());
				modelMap.put("pageTitle", "Update new product");
				modelMap.put("action", "update");
				modelMap.put("pathAction", relativePath + "/update");
				modelMap.put("product", product);

				return "backend/product-add";
			}

		} catch (ProductCategoryNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:" + productCategoryPath;
		} catch (ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:" + relativePath;
		}
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public String processDeleteProvince(@RequestParam(value = "id") Integer productId,
			RedirectAttributes redirectAttributes) {

		try {
			productService.delete(productId);
			redirectAttributes.addFlashAttribute("success", true);
		} catch (ProductNotFoundException|ProductHasCommentException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}

		return "redirect:" + relativePath;
	}

}
