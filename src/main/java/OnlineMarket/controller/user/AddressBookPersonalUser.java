package OnlineMarket.controller.user;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Address;
import OnlineMarket.service.AddressService;
import OnlineMarket.service.ProvinceService;
import OnlineMarket.util.exception.AddressNotFoundException;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.exception.address.AddressHasOrderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/user/{userId:\\d+}/address")
public class AddressBookPersonalUser extends UserControllerInterface {

    @Autowired
    AddressService addressService;

    @Autowired
    ProvinceService provinceService;

    @Override
    public ModelMap modelMap(ModelMap modelMap){
        relativePath = userProfilePath+"/address";
        modelMap.put("addressPage", true);
        modelMap.put("pathAdd", relativePath+"/add");
        breadcrumbs.add(new String[]{relativePath, "Address book"});
        return modelMap;
    }

    @RequestMapping("")
    public String addressPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap, HttpServletRequest request) throws NoHandlerFoundException {

        try {
            modelMap.put("pageTitle", "Address Book");
            modelMap.put("result", addressService.listByUser(user, filterForm));
            modelMap.put("filterForm", filterForm);
            modelMap.put("relativePath", relativePath);

        } catch (CustomException ex) {
            throw new NoHandlerFoundException(request.getMethod(), request.getRequestURL().toString(), null);
        }

        return "user/address-book";
    }

    @RequestMapping("/page/{page:\\d+}")
    public String paginationAddressPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap, HttpServletRequest request, @PathVariable("page") Integer page) throws NoHandlerFoundException {

        try {
            filterForm.setCurrentPage(page);
            modelMap.put("pageTitle", "Address Book");
            modelMap.put("result", addressService.listByUser(user, filterForm));
            modelMap.put("filterForm", filterForm);
            modelMap.put("relativePath", relativePath);

        } catch (CustomException ex) {
            throw new NoHandlerFoundException(request.getMethod(), request.getRequestURL().toString(), null);
        }

        return "user/address-book";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addAddressPage(
            ModelMap modelMap) {

        modelMap.put("pageTitle", "New address");
        modelMap.put("subPageTitle", "Add a address to "+currentUser.getFirstName()+"'s Address Book");
        modelMap.put("description", "Enter information user address");
        modelMap.put("path", "add-address");
        modelMap.put("action", "add");
        modelMap.put("pathAction", relativePath + "/add");
        modelMap.put("provinceList", provinceService.list());
        modelMap.put("addressForm", new Address());

        return "user/address-add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddAddressPage(
            @Valid @ModelAttribute("addressForm") Address address, BindingResult result,
            HttpServletRequest request,
            ModelMap modelMap, RedirectAttributes redirectAttributes) throws NoHandlerFoundException {

        try {
            breadcrumbs.add(new String[]{relativePath, "Address book"});
            modelMap.put("pathAdd", relativePath+"/add");
            if (!result.hasErrors()) {
                addressService.save(address, user);
                redirectAttributes.addFlashAttribute("success", true);
                return "redirect:" + relativePath+"/update/"+address.getId();
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
            throw new NoHandlerFoundException(request.getMethod(), request.getRequestURL().toString(), null);
        }

    }

    @RequestMapping(value = "/update/{addressId:\\d+}", method = RequestMethod.GET)
    public String updatePage(
            @PathVariable("userId") Integer userId,
            @PathVariable("addressId") Integer addressId, ModelMap modelMap,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes){
        try {
            Address address = addressService.getByKey(addressId);
            if(address == null) throw new AddressNotFoundException();
            if(request.isUserInRole("USER") && !address.getUser().getId().equals(userId)) throw new CustomException("Access is dined");
            modelMap.put("pageTitle", "Update address for " + user.getFirstName());
            modelMap.put("subPageTitle", "Update");
            modelMap.put("description", "Enter information user address");
            modelMap.put("path", "update-address");
            modelMap.put("action", "update");
            modelMap.put("pathAction", relativePath + "/update");
            modelMap.put("provinceList", provinceService.list());
            modelMap.put("addressForm", address);

        } catch (AddressNotFoundException|CustomException addressNotFoundException) {
            redirectAttributes.addFlashAttribute("error", addressNotFoundException.getMessage());
            return "redirect:" + relativePath;
        }

        return "user/address-add";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdatePage(
            @PathVariable("userId") Integer userId,
            @Valid @ModelAttribute("addressForm") Address address, BindingResult result,
            HttpServletRequest request,
            ModelMap modelMap, RedirectAttributes redirectAttributes){

        try {
            if(!result.hasErrors()){
                Address addressCheck = addressService.getByKey(address.getId());
                if(request.isUserInRole("USER") && !addressCheck.getUser().getId().equals(userId)) throw new CustomException("Access is dined");
                addressService.update(address, user);
                redirectAttributes.addFlashAttribute("success", true);
                return "redirect:" + relativePath + "/update/" + address.getId();
            }

            modelMap.put("pageTitle", "Update address for " + user.getFirstName());
            modelMap.put("subPageTitle", "Update");
            modelMap.put("description", "Enter information user address");
            modelMap.put("path", "update-address");
            modelMap.put("action", "update");
            modelMap.put("pathAction", relativePath + "/update");
            modelMap.put("provinceList", provinceService.list());
            modelMap.put("address", address);

        } catch (AddressNotFoundException|CustomException addressNotFoundException) {
            redirectAttributes.addFlashAttribute("error", addressNotFoundException.getMessage());
            return "redirect:" + relativePath;
        }

        return "user/address-add";
    }


    @RequestMapping("/delete")
    public String processDeleteProvince(
            @PathVariable("userId") Integer userId,
            @RequestParam(value = "id") Integer idAddress,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        try {
            Address address = addressService.getByKey(idAddress);
            if(address == null) throw new AddressNotFoundException();
            if(request.isUserInRole("ADMIN")
                    || request.isUserInRole("SUPER_ADMIN")
                    || address.getUser().getId().equals(userId))
                addressService.delete(idAddress);
            else throw new CustomException("Access is denied");
            redirectAttributes.addFlashAttribute("success", "success");
        } catch (AddressHasOrderException | AddressNotFoundException | CustomException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:" + relativePath;
    }
}
