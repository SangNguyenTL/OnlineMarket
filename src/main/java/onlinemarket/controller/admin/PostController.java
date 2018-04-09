package onlinemarket.controller.admin;

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.form.filter.FilterPost;
import onlinemarket.model.Post;
import onlinemarket.service.PostCategoryService;
import onlinemarket.service.PostService;
import onlinemarket.util.exception.post.PostHasCommentException;
import onlinemarket.util.exception.post.PostNotFoundException;
import onlinemarket.util.exception.postCategory.PostCategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/{postType:(?:post|page)}")
public class PostController extends MainController {

    private FilterForm filterForm;

    private FilterPost filterPost;

    private String postType;

    @Autowired
    private PostService postService;

    @Autowired
    private PostCategoryService postCategoryService;

    @ModelAttribute
    public ModelMap populateAttribute(@PathVariable("postType") String postType, ModelMap model) {
        filterForm = new FilterForm();
        this.postType = postType;
        title = String.valueOf(postType.charAt(0)).toUpperCase() + postType.substring(1);
        filterForm.getGroupSearch().put("postType", postType);
        filterPost = new FilterPost(filterForm);
        title = "Post manager";
        relativePath = "/admin/post";
        generateBreadcrumbs();
        breadcrumbs.add(new String[]{"/admin", "Admin"});
        breadcrumbs.add(new String[]{relativePath, title});
        model.put("relativePath", relativePath);
        model.put("filterForm", filterForm);
        model.put("filterPost", filterPost);
        model.put("pathAdd", relativePath+"/add");
        if(postType.equals("post"))
            model.put("postPage", true);
        if(postType.equals("page"))
            model.put("pagePage", true);
        model.put("postCategoryList", postCategoryService.list());

        return model;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@ModelAttribute("filterPost") FilterPost filterPost, ModelMap modelMap){

        filterPost.setFilterForm(filterForm);
        if(filterPost.getStatus() != null){
            filterForm.getGroupSearch().put("status", filterPost.getStatus());
        }
        if(filterPost.getPostCategory() != null){
            filterForm.getGroupSearch().put("postCategory.name", filterPost.getPostCategory().getName());
        }

        modelMap.put("result", postService.list(filterForm));
        modelMap.put("pageTitle", title+" manager");
        modelMap.put("filterPost", filterPost);
        modelMap.put("filterForm", filterForm);

        return "backend/post";
    }

    @RequestMapping(value = "/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePagination(
            @ModelAttribute("filterPost") FilterPost filterPost,
            @PathVariable("page") Integer page, ModelMap modelMap) {

        filterForm.setCurrentPage(page);
        filterPost.setFilterForm(filterForm);
        if(filterPost.getStatus() != null){
            filterForm.getGroupSearch().put("status", filterPost.getStatus());
        }
        if(filterPost.getPostCategory() != null){
            filterForm.getGroupSearch().put("postCategory.name", filterPost.getPostCategory().getName());
        }

        modelMap.put("result", postService.list(filterForm));
        modelMap.put("pageTitle", title+" manager");
        modelMap.put("filterPost", filterPost);
        modelMap.put("filterForm", filterForm);

        return "backend/postCategory";
    }

    @RequestMapping( value = "/add", method = RequestMethod.GET)
    public String addPage(ModelMap modelMap){

        modelMap.put("subPageTitle", "Add");
        modelMap.put("pageTitle", "Add new "+ postType);
        modelMap.put("action", "add");
        modelMap.put("pathAction", relativePath + "/add");
        modelMap.put("post", new Post());

        return "backend/post-add";
    }

    @RequestMapping( value = "/add", method = RequestMethod.POST)
    public String processAddPage(@Valid @ModelAttribute("post") Post post, ModelMap modelMap, RedirectAttributes redirectAttributes, BindingResult result){
        try{

            if(!result.hasErrors()){
                postService.save(post, currentUser);
                redirectAttributes.addFlashAttribute("success", true);
            }else{
                modelMap.put("subPageTitle", "Add");
                modelMap.put("pageTitle", "Add new "+ postType);
                modelMap.put("action", "add");
                modelMap.put("pathAction", relativePath + "/add");
                modelMap.put("post", post);

                return "backend/post-add";
            }

        }catch (PostCategoryNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:" + relativePath;
    }

    @RequestMapping(value = "/update/{postId:^\\d+}", method = RequestMethod.GET)
    public String updatePage(@PathVariable("postId") Integer postId, ModelMap modelMap, RedirectAttributes redirectAttributes) {

        try {
            Post post = postService.getByKey(postId);
            if(post == null) throw new PostNotFoundException();

            modelMap.put("subPageTitle", "Update new "+ postType);
            modelMap.put("pageTitle", "Update");
            modelMap.put("action", "update");
            modelMap.put("pathAction", relativePath + "/update");
            modelMap.put("post", post);

        } catch (PostNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + relativePath;
        }

        return "backend/post-add";

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdatePage(@ModelAttribute("post") Post post, ModelMap modelMap,BindingResult result,RedirectAttributes redirectAttributes) {

        try {
            if(!result.hasErrors()){
                postService.update(post, currentUser);
                redirectAttributes.addFlashAttribute("success", true);
                return "redirect:"+ relativePath;
            }else{
                modelMap.put("subPageTitle", "Update new "+ postType);
                modelMap.put("pageTitle", "Update");
                modelMap.put("action", "update");
                modelMap.put("pathAction", relativePath + "/update");
                modelMap.put("post", post);

                return "backend/post-add";
            }

        }catch (PostCategoryNotFoundException|PostNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + relativePath;
        }
    }

    @RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
    public String processDeleteProvince(@RequestParam(value = "id") Integer postId,
                                        RedirectAttributes redirectAttributes) {

        try {
            postService.delete(postId);
        } catch (PostNotFoundException|PostHasCommentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:" + relativePath;
    }

}
