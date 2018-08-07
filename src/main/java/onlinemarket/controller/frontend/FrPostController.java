package onlinemarket.controller.frontend;

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.PostCategory;
import onlinemarket.service.PostCategoryService;
import onlinemarket.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

@Controller
@RequestMapping("/post-category")
public class FrPostController extends MainController {

    @Autowired
    PostService postService;

    @Autowired
    PostCategoryService postCategoryService;

    PostCategory postCategory;

    FilterForm filterForm;

    String subTitle;

    @ModelAttribute
    public ModelMap populateAttribute(ModelMap model) {

        filterForm = new FilterForm();

        filterForm.getGroupSearch().put("status", "0");
        filterForm.setOrderBy("createDate");
        filterForm.setOrder("desc");
        title = "Post list";
        generateBreadcrumbs();
        relativePath = "/post-category";
        model.put("title", title);
        breadcrumbs.add(new String[]{relativePath, "Post"});

        model.put("filterForm", filterForm);
        model.put("relativePath", relativePath);
        model.put("postCategoryList", postCategoryService.list());

        return model;
    }

    @RequestMapping( value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap){
        modelMap.put("result", postService.list(filterForm));
        return "frontend/post-list";
    }


    @RequestMapping( value = "/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePagination(@PathVariable("page") Integer page, @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap){

        filterForm.setCurrentPage(page);
        modelMap.put("result", postService.list(filterForm));
        return "frontend/post-list";
    }

    @RequestMapping( value = "/{postCategorySlug:[\\w\\d-]+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePostCategory(@PathVariable("postCategorySlug") String postCategorySlug,  @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) throws NoHandlerFoundException {

        postCategory = postCategoryService.getByDeclaration("slug", postCategorySlug);
        if(postCategory == null) throw new NoHandlerFoundException(null, null, null);
        filterForm.getGroupSearch().put("postCategory.slug", postCategory.getSlug());

        subTitle = postCategory.getName();
        relativePath = relativePath + "/" + postCategorySlug;
        breadcrumbs.add(new String[]{relativePath, postCategory.getName()});

        modelMap.put("result", postService.list(filterForm));
        modelMap.put("relativePath", relativePath);
        modelMap.put("title", title);
        modelMap.put("subTitle", subTitle);
        return "frontend/post-list";
    }

    @RequestMapping( value = "/{postCategorySlug:[\\w\\d-]+}/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePaginationPostCategory(@PathVariable("postCategorySlug") String postCategorySlug, @PathVariable("page") Integer page, @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) throws NoHandlerFoundException {

        postCategory = postCategoryService.getByDeclaration("slug", postCategorySlug);
        if(postCategory == null) throw new NoHandlerFoundException(null, null, null);
        filterForm.getGroupSearch().put("postCategory.slug", postCategory.getSlug());

        subTitle = postCategory.getName();
        relativePath = relativePath + "/" + postCategorySlug;
        breadcrumbs.add(new String[]{relativePath, postCategory.getName()});

        modelMap.put("result", postService.list(filterForm));

        filterForm.setCurrentPage(page);
        modelMap.put("result", postService.list(filterForm));
        modelMap.put("relativePath", relativePath);
        modelMap.put("title", title);
        modelMap.put("subTitle", subTitle);
        return "frontend/post-list";

    }
}
