package onlinemarket.controller.admin;

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Address;
import onlinemarket.model.User;
import onlinemarket.service.AddressService;
import onlinemarket.service.ProvinceService;
import onlinemarket.service.UserService;
import onlinemarket.util.exception.AddressNotFoundException;
import onlinemarket.util.exception.CustomException;
import onlinemarket.util.exception.address.AddressHasOrderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/user/{userId:^\\d}/address-book")
public class AddressBookController extends MainController {

    private FilterForm filterForm;

    private User user;

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    @Autowired
    ProvinceService provinceService;

    private String userPath;

    @ModelAttribute
    public ModelMap populateAttribute(@PathVariable("userId") Integer userId, ModelMap model) {

        user = userService.getByKey(userId);
        if (user != null) {
            title = "Address book of " + user.getFirstName();
            userPath = "/admin/user/" + user.getId();
            relativePath = userPath + "/address-book";
        }
        generateBreadcrumbs();
        breadcrumbs.add(new String[]{"/admin", "Admin"});
        breadcrumbs.add(new String[]{userPath, "User management"});
        breadcrumbs.add(new String[]{relativePath, "Address book"});

        filterForm = new FilterForm();
        model.put("relativePath", relativePath);
        model.put("pathAdd", relativePath+"/add");
        model.put("filterForm", filterForm);
        model.put("user", user);
        model.put("userPage", true);

        return model;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET , RequestMethod.POST})
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap, RedirectAttributes redirectAttributes) {

        try {
            modelMap.put("pageTitle", title);
            modelMap.put("result", addressService.listByUser(user, filterForm));
            modelMap.put("filterForm", filterForm);

        } catch (CustomException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/admin/user";
        }

        return "backend/address-book";
    }

    @RequestMapping(value = "/page/{page:^\\d+}", method = {RequestMethod.GET , RequestMethod.POST})
    public String paginationPage(@ModelAttribute("filterForm") FilterForm filterForm, @PathVariable("page") Integer page, ModelMap modelMap, RedirectAttributes redirectAttributes) {

        try {
            filterForm.setCurrentPage(page);
            modelMap.put("pageTitle", title);
            modelMap.put("result", addressService.listByUser(user, filterForm));
            modelMap.put("filterForm", filterForm);

        } catch (CustomException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/admin/user";
        }

        return "backend/address-book";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addPage(ModelMap modelMap, RedirectAttributes redirectAttributes) {

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/admin/user";
        }
        modelMap.put("pageTitle", "Add address for " + user.getFirstName());
        modelMap.put("subPageTitle", "Add");
        modelMap.put("description", "Enter information user address");
        modelMap.put("path", "add-address");
        modelMap.put("action", "add");
        modelMap.put("pathAction", relativePath + "/add");
        modelMap.put("provinceList", provinceService.list());
        modelMap.put("address", new Address());

        return "backend/address-add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddPage(@Valid @ModelAttribute("address") Address address, BindingResult result, ModelMap modelMap, RedirectAttributes redirectAttributes) {

        try {
            if (!result.hasErrors()) {
                addressService.save(address, user);
                redirectAttributes.addFlashAttribute("success", true);
                return "redirect:" + relativePath;
            }

            modelMap.put("pageTitle", "Add address for " + user.getFirstName());
            modelMap.put("subPageTitle", "Add");
            modelMap.put("description", "Enter information user address");
            modelMap.put("path", "add-address");
            modelMap.put("action", "add");
            modelMap.put("pathAction", relativePath + "/add");
            modelMap.put("provinceList", provinceService.list());

            return "backend/address-add";

        } catch (CustomException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/admin/user";
        }

    }

    @RequestMapping(value = "/update/{addressId:^\\d+}", method = RequestMethod.GET)
    public String updatePage(@PathVariable("addressId") Integer addressId, ModelMap modelMap, RedirectAttributes redirectAttributes) {

        try {
            if(user == null) throw new CustomException("User not found");

            Address address = addressService.getByKey(addressId);

            if(address == null) throw new AddressNotFoundException();

            modelMap.put("pageTitle", "Update address for " + user.getFirstName());
            modelMap.put("subPageTitle", "Update");
            modelMap.put("description", "Enter information user address");
            modelMap.put("path", "update-address");
            modelMap.put("action", "update");
            modelMap.put("pathAction", relativePath + "/update");
            modelMap.put("provinceList", provinceService.list());
            modelMap.put("address", address);

        } catch (CustomException cusEx) {
            redirectAttributes.addFlashAttribute("error", cusEx.getMessage());
            return "redirect:/admin/user";
        } catch (AddressNotFoundException addressNotFoundException) {
            redirectAttributes.addFlashAttribute("error", addressNotFoundException.getMessage());
            return "redirect:" + relativePath;
        }

        return "backend/address-add";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdatePage(@Valid @ModelAttribute("address") Address address, ModelMap modelMap, BindingResult result, RedirectAttributes redirectAttributes) {

        try {

            if(!result.hasErrors()){
                addressService.update(address, user);
                redirectAttributes.addFlashAttribute("success", true);
                return "redirect:" + relativePath;
            }

            modelMap.put("pageTitle", "Update address for " + user.getFirstName());
            modelMap.put("subPageTitle", "Update");
            modelMap.put("description", "Enter information user address");
            modelMap.put("path", "update-address");
            modelMap.put("action", "update");
            modelMap.put("pathAction", relativePath + "/update");
            modelMap.put("provinceList", provinceService.list());
            modelMap.put("address", address);

        } catch (CustomException cusEx) {
            redirectAttributes.addFlashAttribute("error", cusEx.getMessage());
            return "redirect:/admin/user";
        } catch (AddressNotFoundException addressNotFoundException) {
            redirectAttributes.addFlashAttribute("error", addressNotFoundException.getMessage());
            return "redirect:" + relativePath;
        }

        return "backend/address-add";
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String processDeleteProvince(
            @RequestParam(value = "id") Integer idAddress,
            RedirectAttributes redirectAttributes) {

        try {
            addressService.delete(idAddress);
            redirectAttributes.addFlashAttribute("success", "success");
        } catch (AddressHasOrderException | AddressNotFoundException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:" + relativePath;
    }
}
