package onlinemarket.controller.admin;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.validation.groups.Default;
import javax.websocket.server.PathParam;

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
import org.springframework.web.servlet.NoHandlerFoundException;

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Brand;
import onlinemarket.model.other.AdvancedValidation;
import onlinemarket.result.ResultObject;
import onlinemarket.service.BrandService;
import onlinemarket.util.Slugify;

@Controller
@RequestMapping("/admin/brand")
public class BrandManagerController extends MainController {

	@Autowired
	BrandService brandService;
	
	Slugify slg;
	
	@PostConstruct
	public void init() {
		this.slg = new Slugify().withTransliterator(true).withLowerCase(true);
	}
	
	@ModelAttribute
	public void populateFilterForm(ModelMap model) {
		model.put("filterForm", new FilterForm());
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model) {
		
		filterForm.setCurrentPage(1);
		
		ResultObject<Brand> result = brandService.list(filterForm);
		
		model.put("result", result);
		model.put("pageTitle", "Brand Manager");
		model.put("brandPage", true);
		model.put("path", "brand");
		model.put("filterForm", filterForm);
		
		return "backend/brand";
	}

	@RequestMapping(value = "/page/{page:^\\d+}", method = RequestMethod.GET)
	public String mainPagePagination(@ModelAttribute("filterForm") FilterForm filterForm, @PathVariable("page") Integer page, ModelMap model) {
		
		filterForm.setCurrentPage(page);
		
		ResultObject<Brand> result = brandService.list(filterForm);
		
		model.put("result", result);
		model.put("pageTitle", "Brand Manager");
		model.put("brandPage", true);
		model.put("path", "brand");
		model.put("filterForm", filterForm);
		
		return "backend/brand";
	}

	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addPage(ModelMap model) {
		model.put("subPageTitle", "Add");
		model.put("description", "Add information of brand");
		model.put("pageTitle", "Add new brand");
		model.put("productPage", true);
		model.put("path", "brand-add");
		model.put("action", "add");
		model.put("brand", new Brand());
		
		return "backend/brand-add";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String processAddPage(@ModelAttribute("brand") @Validated(value = { Default.class,
			AdvancedValidation.CheckSlug.class }) Brand brand, BindingResult result, ModelMap model) {
		
		String slug = brand.getSlug();
		if (StringUtils.isNotBlank(slug)) {
			brand.setSlug(slg.slugify(slug));
		}
		
		if (!result.hasErrors()) {
			brandService.save(brand);
			return "redirect:/admin/brand";
		}
		
		model.put("subPageTitle", "Add");
		model.put("description", "Add information of brand");
		model.put("pageTitle", "Add new brand");
		model.put("productPage", true);
		model.put("path", "brand-add");
		model.put("action", "add");
		model.put("brand", brand);
		return "backend/brand-add";
	}

	@RequestMapping(value = "/update/{id:^\\d+}", method = RequestMethod.GET)
	public String updatePage(@PathParam("id") Integer id, ModelMap model) throws NoHandlerFoundException {

		Brand brand = brandService.getByKey(id);
		if (brand == null)
			throw new NoHandlerFoundException(null, null, null);

		model.put("pageTitle", "Update brand");
		model.put("subPageTitle", "Update");
		model.put("description", "Update information of brand");
		model.put("productPage", true);
		model.put("path", "brand-add");
		model.put("action", "add");
		model.put("brand", brand);
		
		return "backend/brand-add";
		
	}

	@RequestMapping(value = "/update/{id:^\\d+}", method = RequestMethod.POST)
	public String processUpdatePage(
			@ModelAttribute("brand") @Validated(value = { Default.class,
					AdvancedValidation.CheckSlug.class }) Brand brand,
			@PathParam("id") Integer id,
			BindingResult result, ModelMap model) throws NoHandlerFoundException {
		
		String slug = brand.getSlug();
		if (StringUtils.isNotBlank(slug)) {
			brand.setSlug(slg.slugify(slug));
		}
		
		if (!result.hasErrors()) {
			brand.setUpdateDate(new Date());
			brandService.update(brand);
			return "redirect:/brand/update/"+id;
		}
		
		model.put("pageTitle", "Update brand");
		model.put("subPageTitle", "Update");
		model.put("description", "Update information of brand");
		model.put("productPage", true);
		model.put("path", "brand-add");
		model.put("action", "add");
		model.put("brand", brand);
		
		return "backend/brand-add";
	}
}
