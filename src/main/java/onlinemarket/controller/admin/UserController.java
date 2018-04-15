package onlinemarket.controller.admin;

import onlinemarket.model.other.AdvancedValidation;
import onlinemarket.service.RoleService;
import onlinemarket.service.UserService;
import onlinemarket.util.exception.CustomException;
import onlinemarket.util.exception.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.User;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.groups.Default;

@Controller
@RequestMapping("/admin/user")
public class UserController extends MainController {

    protected FilterForm filterForm;

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    @ModelAttribute
    public ModelMap populateAttribute(ModelMap model) {

        title = "User management page";
        relativePath = "/admin/user";
        filterForm = new FilterForm();
        generateBreadcrumbs();
        breadcrumbs.add(new String[]{"/admin", "Admin"});
        breadcrumbs.add(new String[]{relativePath, "User management page"});
        model.put("relativePath", relativePath);
        model.put("filterForm", filterForm);
        model.put("roles", roleService.list());
        model.put("pathAdd", relativePath+"/add");
        model.put("userPage", true);

        return model;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET , RequestMethod.POST})
    String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model) {

        model.put("pageTitle", title);
        model.put("result", userService.list(filterForm));
        model.put("filterForm", filterForm);
        model.put("path", "user");

        return "backend/user";
    }

    @RequestMapping(value = "/page/{page:^\\d+}", method = {RequestMethod.GET , RequestMethod.POST})
    public String mainPagePagination(@ModelAttribute("filterForm") FilterForm filterForm, @PathVariable("page") Integer page, ModelMap model) {

        filterForm.setCurrentPage(page);

        model.put("pageTitle", title);
        model.put("result", userService.list(filterForm));
        model.put("filterForm", filterForm);
        model.put("path", "user");
        model.addAllAttributes(filterForm.getGroupSearch());

        return "backend/user";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addPage(ModelMap model) {

        model.put("pageTitle", "Add new user");
        model.put("subPageTitle", "Add");
        model.put("description", "Enter information user");
        model.put("path", "user-add");
        model.put("action", "add");
        model.put("pathAction", relativePath+"/add");
        model.put("user", new User());

        return "backend/user-add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddPage(
            @Validated(value = {Default.class, AdvancedValidation.CheckEmail.class}) @ModelAttribute("user") User user,
            BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {

        if (!result.hasErrors()) {
            redirectAttributes.addFlashAttribute("success", true);
            userService.save(user);
            return "redirect:"+relativePath;
        }
        model.put("pageTitle", "Add new user");
        model.put("subPageTitle", "Add");
        model.put("description", "Enter information user");
        model.put("path", "user-add");
        model.put("action", "add");
        model.put("pathAction", relativePath+"/add");
        model.put("user", user);

        return "backend/user-add";
    }

    @RequestMapping(value = "/update/{id:^\\d+}", method = RequestMethod.GET)
    public String updatePage(@PathVariable("id") Integer id, ModelMap model, RedirectAttributes redirectAttributes) {

        User user = userService.getByKey(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:"+relativePath;
        }

        model.put("pageTitle", "Update information user");
        model.put("subPageTitle", "Update");
        model.put("description", "Enter the information you want to change");
        model.put("path", "user-add");
        model.put("action", "update");
        model.put("pathAction", relativePath+"/update");
        model.put("user", user);

        return "backend/user-add";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdatePage(
            @Validated(value = {Default.class, AdvancedValidation.CheckEmail.class}) @ModelAttribute("user") User user, BindingResult result,
            ModelMap model, RedirectAttributes redirectAttributes) {

        try {
            if(!result.hasErrors()){
                userService.update(user);
                redirectAttributes.addFlashAttribute("success", true);
                return "redirect:/admin/user";
            }
            model.put("pageTitle", "Update information user");
            model.put("subPageTitle", "Update");
            model.put("description", "Enter the information you want to change");
            model.put("path", "user-add");
            model.put("action", "update");
            model.put("pathAction", relativePath+"/update");
            model.put("user", user);

            return "backend/user-add";

        } catch (CustomException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:"+relativePath;
        }
    }


    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String processDeleteProvince(
            @RequestParam(value = "id") Integer userId,
            RedirectAttributes redirectAttributes) {

        try {
            userService.delete(userId);
            redirectAttributes.addFlashAttribute("success", "success");
        } catch (UserHasPostException|UserHasEventException|UserNotFoundException|UserHasProductException|UserIsSuperAdminException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:" + relativePath;
    }
}
