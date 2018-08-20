package OnlineMarket.controller.admin;

import javax.validation.groups.Default;

import OnlineMarket.util.exception.postCategory.PostCategoryHasPostException;
import OnlineMarket.util.exception.postCategory.PostCategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import OnlineMarket.controller.MainController;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.PostCategory;
import OnlineMarket.util.group.AdvancedValidation;
import OnlineMarket.service.PostCategoryService;

@Controller
@RequestMapping("/admin/post-category")
public class PostCategoryController extends MainController {
    @Autowired
    PostCategoryService postCategoryService;

    FilterForm filterForm;

    @ModelAttribute
    public ModelMap populateFilterForm(ModelMap model) {
        filterForm = new FilterForm();
        filterForm.setSearchBy("name");
        title = "Post category manager";
        relativePath = "/admin/post-category";
        generateBreadcrumbs();
        breadcrumbs.add(new String[]{"/admin", "Admin"});
        breadcrumbs.add(new String[]{relativePath, "Post category"});
        model.put("relativePath", relativePath);
        model.put("pageTitle", title);
        model.put("filterForm", filterForm);
        model.put("pathAdd", relativePath + "/add");
        model.put("postCategoryPage", true);
        model.put("pageType", "post-category");
        return model;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String mainPage(ModelMap model) {
        model.put("pageTitle", "Post category manager");
        model.put("path", "post-category");
        model.put("result", postCategoryService.list(filterForm));
        model.put("filterForm", filterForm);

        return "backend/post-category";
    }

    @RequestMapping(value = "/page/{page:^\\d+}", method = RequestMethod.GET)
    public String mainPagePagination(@PathVariable("page") Integer page, ModelMap model) {
        filterForm.setCurrentPage(page);
        model.put("pageTitle", "Post category manager");
        model.put("path", "post-category");
        model.put("result", postCategoryService.list(filterForm));
        model.put("filterForm", filterForm);

        return "backend/post-category";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addPage(ModelMap model) {

        model.put("subPageTitle", "Add");
        model.put("description", "Add information of Post Category");
        model.put("pageTitle", "Add new Post Category");
        model.put("path", "post-category-add");
        model.put("action", "add");
        model.put("pathAction", relativePath + "/add");

        model.put("postCategory", new PostCategory());

        return "backend/post-category-add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddPage(@ModelAttribute("postCategory") @Validated(value = {Default.class,
            AdvancedValidation.CheckSlug.class}) PostCategory postCategory, BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {


        if (!result.hasErrors()) {
            postCategoryService.save(postCategory);
            redirectAttributes.addFlashAttribute("success", true);
            return "redirect:" + relativePath+"/update/"+postCategory.getId();
        }

        model.put("subPageTitle", "Add");
        model.put("description", "Add information of Post Category");
        model.put("pageTitle", "Add new Post Category");
        model.put("path", "post-category-add");
        model.put("action", "add");
        model.put("pathAction", relativePath + "/add");
        model.put("postCategory", postCategory);

        return "backend/post-category-add";
    }

    @RequestMapping(value = "/update/{postCategoryId:^\\d+}", method = RequestMethod.GET)
    public String updatePage(@PathVariable("postCategoryId") Integer postCategoryId, ModelMap model, RedirectAttributes redirectAttributes) {

        try {
            PostCategory postCategory = postCategoryService.getByKey(postCategoryId);
            if (postCategory == null)
                throw new PostCategoryNotFoundException();

            model.put("pageTitle", "Update Post Category");
            model.put("subPageTitle", "Update");
            model.put("description", "Update information of Post Category");
            model.put("path", "post-category-add");
            model.put("action", "update");
            model.put("pathAction", relativePath + "/update");
            model.put("postCategory", postCategory);

            return "backend/post-category-add";

        } catch (PostCategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + relativePath;
        }

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdatePage(
            @ModelAttribute("postCategory") @Validated(value = {Default.class, AdvancedValidation.CheckSlug.class}) PostCategory postCategory,
            BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {

        try {
            if (!result.hasErrors()) {
                postCategoryService.update(postCategory);
                redirectAttributes.addFlashAttribute("success", "");
                return "redirect:" + relativePath+"/update/"+postCategory.getId();
            }
            model.put("pageTitle", "Update Post Category");
            model.put("subPageTitle", "Update");
            model.put("description", "Update information of Post Category");
            model.put("path", "post-category-add");
            model.put("action", "update");
            model.put("pathAction", relativePath + "/update");
            model.put("postCategory", postCategory);

            return "backend/post-category-add";
        } catch (PostCategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + relativePath;
        }
    }

    @RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
    public String processDeleteProvince(@RequestParam(value = "id") Integer postId,
                                        RedirectAttributes redirectAttributes) {

        try {
            postCategoryService.delete(postId);
        }catch (PostCategoryHasPostException|PostCategoryNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + relativePath;
        }

        return "redirect:" + relativePath;
    }
}
