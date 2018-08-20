package OnlineMarket.controller.admin;


import javax.validation.groups.Default;

import OnlineMarket.util.group.AdvancedValidation;
import OnlineMarket.util.exception.CustomException;
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


import OnlineMarket.controller.MainController;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Province;
import OnlineMarket.model.Address;
import OnlineMarket.result.ResultObject;
import OnlineMarket.service.AddressService;
import OnlineMarket.service.ProvinceService;

@Controller
@RequestMapping("/admin/province")
public class ProvinceController extends MainController {

    @Autowired
    ProvinceService provinceService;

    @Autowired
    AddressService addressService;

    FilterForm filterForm;

    @ModelAttribute
    public ModelMap modelAttribute(ModelMap model) {
        filterForm = new FilterForm();
        relativePath = "/admin/province";
        model.put("filterForm", filterForm);
        model.put("relativePath", relativePath);
        model.put("provincePage", true);
        model.put("pathAdd", relativePath + "/add");
        breadcrumbs.add(new String[]{ relativePath, "Province"});
        
        return model;

    }

    @RequestMapping(value = "", method = { RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model) {

        model.put("pageTitle", "Province Manager");
        model.put("path", "province");
        model.put("result", provinceService.list(filterForm));
        model.put("filterForm", filterForm);

        return "backend/province";
    }

    @RequestMapping(value = "/page/{page:^\\d+}", method ={ RequestMethod.GET, RequestMethod.POST})
    public String mainPagePagination(@ModelAttribute("filterForm") FilterForm filterForm, @PathVariable("page") Integer page,
                                     ModelMap model) {

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
        model.put("pathAction", relativePath + "/add");
        model.put("province", new Province());

        return "backend/province-add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddPage(@ModelAttribute("province") @Validated(value = {Default.class, AdvancedValidation.AddNew.class}) Province province,
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
        model.put("pathAction", relativePath + "/add");
        model.put("province", province);

        return "backend/province-add";
    }

    @RequestMapping(value = "/update/{id:^\\d+}", method = RequestMethod.GET)
    public String updatePage(@PathVariable("id") Integer id, ModelMap model, RedirectAttributes redirectAttributes) {

        Province province = provinceService.getByKey(id);
        if (province == null) {
            redirectAttributes.addFlashAttribute("error", "Province not found");
            return "redirect:/admin/province";
        }

        model.put("pageTitle", "Update province");
        model.put("subPageTitle", "Update");
        model.put("description", "Update information of province");
        model.put("path", "province-add");
        model.put("action", "update");
        model.put("pathAction", "/admin/province/update");
        model.put("province", province);

        return "backend/province-add";

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdatePage(
            @ModelAttribute("province") @Validated(value = {Default.class, AdvancedValidation.AddNew.class})  Province province,
            BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {

        if (!result.hasErrors()) {
            try {

                provinceService.update(province);
                redirectAttributes.addFlashAttribute("success", "");
                return "redirect:/admin/province";

            } catch (CustomException ex) {
                redirectAttributes.addFlashAttribute("error", ex.getMessage());
                return "redirect:/admin/province";
            }

        }

        model.put("pageTitle", "Update province");
        model.put("subPageTitle", "Update");
        model.put("description", "Update information of province");
        model.put("path", "province-add");
        model.put("action", "update");
        model.put("pathAction", "/admin/province/update");
        model.put("province", province);

        return "backend/province-add";
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String processDeleteProvince(@RequestParam(value = "id", required = true) Integer id, RedirectAttributes redirectAttributes) {

        if (id == null) {
            redirectAttributes.addFlashAttribute("error", "Program isn't get province id!");
            return "redirect:/admin/province";
        }
        Province provinceCheck = provinceService.getByKey(id);
        if (provinceCheck == null) {
            redirectAttributes.addFlashAttribute("error", "The province isn't exist!");
        } else {
            Address address = addressService.getByProvince(provinceCheck);
            if (address != null)
                redirectAttributes.addFlashAttribute("error", "The province has already had address!");
            else {
                provinceService.delete(provinceCheck);
                redirectAttributes.addFlashAttribute("success", "");
            }
        }
        return "redirect:/admin/province";
    }

    public String buildRelativePath(int id) {
        return "/admin/product-category/" + id + "/attribute-group";
    }
}
