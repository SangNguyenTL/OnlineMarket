package OnlineMarket.controller.admin;

import javax.validation.groups.Default;

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
import OnlineMarket.model.Post;
import OnlineMarket.service.PostService;
import OnlineMarket.util.exception.post.PostNotFoundException;
import OnlineMarket.util.exception.postCategory.PostCategoryNotFoundException;
import OnlineMarket.util.group.AdvancedValidation;

@Controller
@RequestMapping("/admin/{postType:(?:page)}")
public class PageController extends MainController {

    private FilterForm filterForm;

    private String postType;

    @Autowired
    private PostService postService;

    @ModelAttribute
    public ModelMap modelAttribute(@PathVariable("postType") String postType, ModelMap model) {
        filterForm = new FilterForm();
        this.postType = postType;
        title = "Page";
        filterForm.getGroupSearch().put("postType", postType);
        relativePath = "/admin/page";

        generateBreadcrumbs();
        breadcrumbs.add(new String[]{"/admin", "Admin"});
        breadcrumbs.add(new String[]{relativePath, title+" manager"});
        model.put("relativePath", relativePath);
        model.put("filterForm", filterForm);
        model.put("uploadType", "page");
        model.put("pathAdd", relativePath + "/add");
        model.put("pagePage", true);

        return model;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) {

        modelMap.put("result", postService.list(filterForm));
        modelMap.put("pageTitle", title + " manager");
        modelMap.put("path", "page");

        return "backend/post";
    }

    @RequestMapping(value = "/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePagination(
            @ModelAttribute("filterForm") FilterForm filterForm,
            @PathVariable("page") Integer page, ModelMap modelMap) {

        filterForm.setCurrentPage(page);

        modelMap.put("result", postService.list(filterForm));
        modelMap.put("pageTitle", title + " manager");
        modelMap.put("path", "page");

        return "backend/post";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addPage(ModelMap modelMap) {

        modelMap.put("subPageTitle", "Add");
        modelMap.put("pageTitle", "Add new page");
        modelMap.put("action", "add");
        modelMap.put("pathAction", relativePath + "/add");
        modelMap.put("path", "page-add");
        modelMap.put("post", new Post());

        return "backend/post-add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddPage(@Validated(value = {Default.class, AdvancedValidation.CheckSlug.class}) @ModelAttribute("post") Post post, BindingResult result, ModelMap modelMap, RedirectAttributes redirectAttributes) {
        try {
            if (!result.hasErrors()) {
                postService.save(post, currentUser, postType);
                redirectAttributes.addFlashAttribute("success", true);
                return "redirect:" + relativePath +"/update/"+post.getId();
            }
            modelMap.put("subPageTitle", "Add");
            modelMap.put("pageTitle", "Add new page");
            modelMap.put("action", "add");
            modelMap.put("path", "page-add");
            modelMap.put("pathAction", relativePath + "/add");
            modelMap.put("post",post);

            return "backend/post-add";

        } catch (PostCategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + relativePath;
        }

    }


    @RequestMapping(value = "/update/{postId:^\\d+}", method = RequestMethod.GET)
    public String updatePage(@PathVariable("postId") Integer postId, ModelMap modelMap, RedirectAttributes redirectAttributes) {

        try {
            Post post = postService.getByKey(postId);
            if (post == null) throw new PostNotFoundException();

            modelMap.put("subPageTitle", "Update page");
            modelMap.put("pageTitle", "Update");
            modelMap.put("action", "update");
            modelMap.put("pathAction", relativePath + "/update");
            modelMap.put("path", "page-add");
            modelMap.put("post", post);

        } catch (PostNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + relativePath;
        }

        return "backend/post-add";

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdatePage(@Validated(value = {Default.class, AdvancedValidation.CheckSlug.class, AdvancedValidation.AddPost.class}) @ModelAttribute("post") Post post, BindingResult result, ModelMap modelMap, RedirectAttributes redirectAttributes) {
        try {
            if (!result.hasErrors()) {
                postService.update(post, currentUser, postType);
                redirectAttributes.addFlashAttribute("success", true);
                return "redirect:" + relativePath + "/update/"+post.getId();
            } else {
                modelMap.put("subPageTitle", "Update page");
                modelMap.put("pageTitle", "Update");
                modelMap.put("action", "update");
                modelMap.put("path", "page-add");
                modelMap.put("pathAction", relativePath + "/update");
                modelMap.put("post", post);

                return "backend/post-add";
            }

        } catch (PostCategoryNotFoundException | PostNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + relativePath;
        }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String processDeleteProvince(@RequestParam(value = "id") Integer postId,
                                        RedirectAttributes redirectAttributes) {

        try {
            postService.delete(postId);
            redirectAttributes.addFlashAttribute("success", true);
        } catch (PostNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:" + relativePath;
    }
}
