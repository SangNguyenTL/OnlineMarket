package onlinemarket.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Province;
import onlinemarket.result.ResultObject;
import onlinemarket.service.ProvinceService;

@Controller
@RequestMapping("/admin/province")
public class ProvinceManagerController extends MainController{

	@Autowired
	ProvinceService provinceService;
	
	@ModelAttribute
	public ModelMap populateAttribute(ModelMap model) {
		
		model.put("filterForm", new FilterForm());
		
		return model;
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model){
		
		ResultObject<Province> result = provinceService.list(filterForm);
		
		model.put("pageTitle", "Province Manager");
		model.put("path", "province");
		model.put("provincePage", true);
		model.put("result", result);
		model.put("filterForm", filterForm);
		
		return "backend/province";
	}
	
	@RequestMapping(value = "/page/{page:^\\d+}", method = RequestMethod.GET)
	public String mainPagePagination(@PathVariable("page") Integer page, @ModelAttribute("filterForm") FilterForm filterForm, ModelMap model){
		
		filterForm.setCurrentPage(page);
		
		ResultObject<Province> result = provinceService.list(filterForm);
		
		model.put("pageTitle", "Province Manager");
		model.put("path", "province");
		model.put("page", page);
		model.put("provincePage", true);
		model.put("result", result);
		model.put("filterForm", filterForm);
		
		return "backend/province";
	}
}
