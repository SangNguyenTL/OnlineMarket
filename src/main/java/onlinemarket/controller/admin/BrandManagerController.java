package onlinemarket.controller.admin;

import java.util.Date;

import javax.validation.groups.Default;

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
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.model.Product;
import onlinemarket.model.other.AdvancedValidation;
import onlinemarket.result.ResultObject;
import onlinemarket.service.BrandService;
import onlinemarket.service.EventService;
import onlinemarket.service.ProductService;

@Controller
@RequestMapping("/admin/brand")
public class BrandManagerController extends MainController {

	@Autowired
	BrandService brandService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	EventService eventService;
	
	@ModelAttribute
	public void populateFilterForm(ModelMap model) {
		model.put("filterForm", new FilterForm());
		model.put("brandPage", true);
		model.put("pathAdd", "/admin/brand/add");
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model) {
			
		ResultObject<Brand> result = brandService.list(filterForm);
		
		model.put("result", result);
		model.put("pageTitle", "Brand Manager");
		model.put("path", "brand");
		model.put("filterForm", filterForm);
		
		return "backend/brand";
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public String processDeleteBrand(@RequestParam(value = "id", required = true) Integer id, RedirectAttributes redirectAttributes) {
			
		if(id == null) {
			redirectAttributes.addAttribute("error", "Program isn't get brand id!");
			return "redirect:/admin/brand";
		}
		Brand brandCheck = brandService.getByKey(id);
		if (brandCheck == null) {
			redirectAttributes.addAttribute("error", "The brand isn't exist!");
		}else {
			Product product = productService.getByBrand(brandCheck);
			if(product != null) 
				redirectAttributes.addAttribute("error", "The brand has already had product!");
			else {
				Event event = eventService.getByBrand(brandCheck);
				if(event != null) 
					redirectAttributes.addAttribute("error", "The brand has already had event!");
				else {
					brandService.delete(brandCheck);
					redirectAttributes.addAttribute("success", "");
				}
			}
		}
		return "redirect:/admin/brand";
	}

	@RequestMapping(value = "/page/{page:^\\d+}", method = RequestMethod.GET)
	public String mainPagePagination(@ModelAttribute("filterForm") FilterForm filterForm, @PathVariable("page") Integer page, ModelMap model) {
		
		filterForm.setCurrentPage(page);
		
		ResultObject<Brand> result = brandService.list(filterForm);
		
		model.put("result", result);
		model.put("page", page);
		model.put("pageTitle", "Brand Manager");
		model.put("path", "brand");
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
		model.put("pathAction", "/admin/brand/add");
		model.put("brand", new Brand());
		
		return "backend/brand-add";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String processAddPage(@ModelAttribute("brand") @Validated(value = { Default.class,
			AdvancedValidation.CheckSlug.class }) Brand brand, BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {
		
		String slug = brand.getSlug();
		if (StringUtils.isNotBlank(slug)) {
			brand.setSlug(slg.slugify(slug));
		}
		
		if (!result.hasErrors()) {
			redirectAttributes.addAttribute("success", "");
			brandService.save(brand);
			return "redirect:/admin/brand";
		}
		model.put("subPageTitle", "Add");
		model.put("description", "Add information of brand");
		model.put("pageTitle", "Add new brand");
		model.put("path", "brand-add");
		model.put("action", "add");
		model.put("pathAction", "/admin/brand/add");
		model.put("brand", brand);
		
		return "backend/brand-add";
	}

	@RequestMapping(value = "/update/{id:^\\d+}", method = RequestMethod.GET)
	public String updatePage(@PathVariable("id") int id, ModelMap model) throws NoHandlerFoundException {

		Brand brand = brandService.getByKey(id);
		if (brand == null)
			throw new NoHandlerFoundException(null, null, null);

		model.put("pageTitle", "Update brand");
		model.put("subPageTitle", "Update");
		model.put("description", "Update information of brand");
		model.put("path", "brand-add");
		model.put("action", "update");
		model.put("pathAction", "/admin/brand/update/"+id);
		model.put("brand", brand);
		
		return "backend/brand-add";
		
	}

	@RequestMapping(value = "/update/{id:^\\d+}", method = RequestMethod.POST)
	public String processUpdatePage(
			@ModelAttribute("brand") @Validated(value = { Default.class,
					AdvancedValidation.CheckSlug.class }) Brand brand,
			@PathVariable("id") Integer id,
			BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) throws NoHandlerFoundException {
		
			Brand brandCheck = brandService.getByKey(id);
			if (brandCheck == null)
				throw new NoHandlerFoundException(null, null, null);
			
			String slug = brand.getSlug();
			if (StringUtils.isNotBlank(slug)) {
				brand.setSlug(slg.slugify(slug));
			}
			
			if (!result.hasErrors()) {
				brand.setUpdateDate(new Date());
				brandService.update(brand);
				redirectAttributes.addAttribute("success", "");
				return "redirect:/brand/update/"+id;
			}
			
			model.put("pageTitle", "Update brand");
			model.put("subPageTitle", "Update");
			model.put("description", "Update information of brand");
			model.put("path", "brand-add");
			model.put("action", "update");
			model.put("pathAction", "/admin/brand/update/"+id);
			model.put("brand", brand);
			
			return "backend/brand-add";
	}
}
