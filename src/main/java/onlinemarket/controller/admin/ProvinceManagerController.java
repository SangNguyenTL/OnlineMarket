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
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Province;
import onlinemarket.model.Address;
import onlinemarket.result.ResultObject;
import onlinemarket.service.AddressService;
import onlinemarket.service.ProvinceService;

@Controller
@RequestMapping("/admin/province")
public class ProvinceManagerController extends MainController{

	@Autowired
	ProvinceService provinceService;
	
	@Autowired
	AddressService addressService;
	
	FilterForm filterForm;
	
	@ModelAttribute
	public ModelMap populateAttribute(ModelMap model) {
		filterForm = new FilterForm();
		model.put("filterForm", filterForm);
		model.put("provincePage", true);
		model.put("pathAdd", "/admin/province/add");
		return model;
		
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String mainPage(ModelMap model){
		
		model.put("pageTitle", "Province Manager");
		model.put("path", "province");
		model.put("result", provinceService.list(filterForm));
		model.put("filterForm", filterForm);
		
		return "backend/province";
	}
	
	@RequestMapping(value = "/page/{page:^\\d+}", method = RequestMethod.GET)
	public String mainPagePagination(@PathVariable("page") Integer page,
			ModelMap model){
		
		filterForm.setCurrentPage(page);
		
		ResultObject<Province> result = provinceService.list(filterForm);
		
		model.put("pageTitle", "Province Manager");
		model.put("path", "province");
		model.put("page", page);
		model.put("result", result);
		model.put("filterForm", filterForm);
		
		return "backend/province";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addPage(ModelMap model) {
		
		model.put("subPageTitle", "Add");
		model.put("description", "Add information of province");
		model.put("pageTitle", "Add new province");
		model.put("provincePage", true);
		model.put("path", "province-add");
		model.put("action", "add");
		model.put("pathAction", "/admin/province/add");
		model.put("province", new Province());
		
		return "backend/province-add";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String processAddPage(@ModelAttribute("province") @Valid Province province,
			BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {

		if (!result.hasErrors()) {
			provinceService.save(province);
			redirectAttributes.addFlashAttribute("success", "");
			return "redirect:/admin/province";
		}
		model.put("subPageTitle", "Add");
		model.put("description", "Add information of province");
		model.put("pageTitle", "Add new province");
		model.put("path", "province-add");
		model.put("action", "add");
		model.put("pathAction", "/admin/province/add");
		model.put("province", province);
		
		return "backend/province-add";
	}

	@RequestMapping(value = "/update/{id:^\\d+}", method = RequestMethod.GET)
	public String updatePage(@PathVariable("id") Integer id, ModelMap model) throws NoHandlerFoundException {

		Province province = provinceService.getByKey(id);
		if (province == null)
			throw new NoHandlerFoundException(null, null, null);

		model.put("pageTitle", "Update province");
		model.put("subPageTitle", "Update");
		model.put("description", "Update information of province");
		model.put("path", "province-add");
		model.put("action", "update");
		model.put("pathAction", "/admin/province/update/"+id);
		model.put("province", province);
		
		return "backend/province-add";
		
	}

	@RequestMapping(value = "/update/{id:^\\d+}", method = RequestMethod.POST)
	public String processUpdatePage(
			@ModelAttribute("province") @Valid Province province,
			@PathVariable("id") Integer id,
			BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) throws NoHandlerFoundException {
		
			Province provinceCheck = provinceService.getByKey(id);
			if (provinceCheck == null)
				throw new NoHandlerFoundException(null, null, null);
			
			if (!result.hasErrors()) {
				provinceService.update(province);
				redirectAttributes.addFlashAttribute("success", "");
				return "redirect:/admin/province";
			}
			
			model.put("pageTitle", "Update province");
			model.put("subPageTitle", "Update");
			model.put("description", "Update information of province");
			model.put("path", "province-add");
			model.put("action", "update");
			model.put("pathAction", "/admin/province/update/"+id);
			model.put("province", province);
			
			return "backend/province-add";
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public String processDeleteProvince(@RequestParam(value = "id", required = true) Integer id, RedirectAttributes redirectAttributes) {
			
		if(id == null) {
			redirectAttributes.addAttribute("error", "Program isn't get province id!");
			return "redirect:/admin/province";
		}
		Province provinceCheck = provinceService.getByKey(id);
		if (provinceCheck == null) {
			redirectAttributes.addAttribute("error", "The province isn't exist!");
		}else {
			Address address = addressService.getByProvince(provinceCheck);
			if(address != null) 
				redirectAttributes.addAttribute("error", "The province has already had address!");
			else {
				provinceService.delete(provinceCheck);
				redirectAttributes.addAttribute("success", "");
			}
		}
		return "redirect:/admin/province";
	}
	
	public String buildRelativePath(int id) {
		return "/admin/product-category/"+id+"/attribute-group";
	}
}
