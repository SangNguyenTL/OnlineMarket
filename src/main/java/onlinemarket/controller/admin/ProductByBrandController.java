package onlinemarket.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Brand;
import onlinemarket.service.AttributeGroupService;
import onlinemarket.service.BrandService;
import onlinemarket.service.ProductService;

@Controller
@RequestMapping("/admin/brand/{id:^\\d}/product")
public class ProductByBrandController extends MainController {

	@Autowired
	ProductService productService;

	@Autowired
	BrandService brandService;

	@Autowired
	AttributeGroupService attributeGroupService;

	Brand brand;

	String relativePath;

	FilterForm filterForm;

	@ModelAttribute
	public ModelMap populateAttribute(@PathVariable("id") Integer id, ModelMap model) {
		relativePath = buildRelativePath(id);
		brand = brandService.getByKey(id);
		filterForm = new FilterForm();
		model.put("productPage", true);
		model.put("path", brand.getSlug() + "-add");
		model.put("brand", brand);
		model.put("filterForm", filterForm);
		model.put("relativePath", relativePath);
		model.put("pathAdd", relativePath + "/add");

		return model;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String mainPage(ModelMap model, RedirectAttributes redirectAttributes) {

		if (brand == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/brand";
		}

		model.put("pageTitle", "Product manager for " + brand.getName());
		model.put("result", productService.listByBrand(brand, filterForm));
		model.put("filterForm", filterForm);

		return "backend/product";
	}

	@RequestMapping(value = "/page/{page:^\\d+}", method = RequestMethod.GET)
	public String mainPagePagination(@PathVariable("page") Integer page, ModelMap model,
			RedirectAttributes redirectAttributes) {

		filterForm.setCurrentPage(page);
		filterForm.setOrderBy("priority");
		filterForm.setOrder("asc");

		if (brand == null) {
			redirectAttributes.addFlashAttribute("error", "Product category not found.");
			return "redirect:/admin/brand";
		}

		model.put("pageTitle", "Product manager for " + brand.getName());
		model.put("result", productService.listByBrand(brand, filterForm));
		model.put("filterForm", filterForm);

		return "backend/product";
	}

	public String buildRelativePath(int id) {
		return "/admin/product-category/" + id + "/product";
	}

}
