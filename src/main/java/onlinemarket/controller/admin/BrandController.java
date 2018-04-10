package onlinemarket.controller.admin;

import java.util.Date;

import javax.validation.groups.Default;

import onlinemarket.util.exception.brand.BrandHasEventException;
import onlinemarket.util.exception.brand.BrandHasProductException;
import onlinemarket.util.exception.brand.BrandNotFoundException;
import org.apache.commons.lang3.StringUtils;
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
import onlinemarket.model.Brand;
import onlinemarket.model.other.AdvancedValidation;
import onlinemarket.service.BrandService;

@Controller
@RequestMapping("/admin/brand")
public class BrandController extends MainController {

	@Autowired
	BrandService brandService;

	FilterForm filterForm;

	@ModelAttribute
	public void populateFilterForm(ModelMap model) {

		filterForm = new FilterForm();
		generateBreadcrumbs();
		title = "Brand manager";
		relativePath = "/admin/brand";
		generateBreadcrumbs();
		breadcrumbs.add(new String[]{"/admin", "Admin"});
		breadcrumbs.add(new String[]{relativePath, "Brand"});
		model.put("relativePath", relativePath);
		model.put("pageTitle", title);
		model.put("filterForm", filterForm);
		model.put("pathAdd", relativePath+"/add");
		model.put("brandPage", true);

	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model) {

		model.put("result",  brandService.list(filterForm));
		model.put("filterForm", filterForm);

		return "backend/brand";
	}

	@RequestMapping(value = "/page/{page:^\\d+}", method = RequestMethod.GET)
	public String mainPagePagination(@ModelAttribute("filterForm") FilterForm filterForm, @PathVariable("page") Integer page, ModelMap model) {

		filterForm.setCurrentPage(page);

		model.put("result", brandService.list(filterForm));
		model.put("filterForm", filterForm);

		return "backend/brand";
	}


	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addPage(ModelMap model) {

		model.put("subPageTitle", "Add");
		model.put("description", "Add information of brand");
		model.put("pageTitle", "Add new brand");
		model.put("path", "brand-add");
		model.put("action", "add");
		model.put("pathAction", relativePath + "/add");
		model.put("brand", new Brand());

		return "backend/brand-add";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String processAddPage( @Validated(value = { Default.class, AdvancedValidation.CheckSlug.class })
			@ModelAttribute("brand") Brand brand,
			BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {

		if (!result.hasErrors()) {
			redirectAttributes.addFlashAttribute("success", "");
			brandService.save(brand);
			return "redirect:/admin/brand";
		}
		model.put("subPageTitle", "Add");
		model.put("description", "Add information of brand");
		model.put("pageTitle", "Add new brand");
		model.put("path", "brand-add");
		model.put("action", "add");
		model.put("pathAction", relativePath + "/add");
		model.put("brand", brand);

		return "backend/brand-add";
	}

	@RequestMapping(value = "/update/{brandId:^\\d+}", method = RequestMethod.GET)
	public String updatePage(@PathVariable("brandId") int brandId, ModelMap model, RedirectAttributes redirectAttributes) {

		Brand brand = brandService.getByKey(brandId);
		try {
			if (brand == null)
				throw new BrandNotFoundException();

			model.put("pageTitle", "Update brand");
			model.put("subPageTitle", "Update");
			model.put("description", "Update information of brand");
			model.put("path", "brand-add");
			model.put("action", "update");
			model.put("pathAction", relativePath + "/update" );
			model.put("brand", brand);

			return "backend/brand-add";

		} catch (BrandNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:" + relativePath;
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String processUpdatePage(
			@Validated(value = { Default.class, AdvancedValidation.CheckSlug.class })
			@ModelAttribute("brand") Brand brand,
			BindingResult result, ModelMap model,
			RedirectAttributes redirectAttributes) {

		try {

			if (!result.hasErrors()) {
				brandService.update(brand);
				redirectAttributes.addFlashAttribute("success", "");
			}else {
				model.put("pageTitle", "Update brand");
				model.put("subPageTitle", "Update");
				model.put("description", "Update information of brand");
				model.put("path", "brand-add");
				model.put("action", "update");
				model.put("pathAction", relativePath + "/update" );
				model.put("brand", brand);

				return "backend/brand-add";
			}

		} catch (BrandNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}

		return "redirect:" + relativePath;
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public String processDeleteBrand(@RequestParam(value = "id") Integer id,
			RedirectAttributes redirectAttributes) {

		try {
			brandService.delete(id);
			redirectAttributes.addFlashAttribute("success", "success");

		} catch (BrandNotFoundException|BrandHasProductException|BrandHasEventException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:" + relativePath;
	}

}
