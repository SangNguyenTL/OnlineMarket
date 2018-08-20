package OnlineMarket.controller.user;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.service.CommentService;
import OnlineMarket.util.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user/{userId:\\d+}/comment")
public class UserCommentController extends UserControllerInterface {

    @Autowired
    private CommentService commentService;

    @Override
    public ModelMap modelMap(ModelMap modelMap) {
        FilterForm filterForm = new FilterForm();
        filterForm.setSearchBy("post.name");
        relativePath = userProfilePath+"/comment";
        modelMap.put("filterForm", filterForm);
        modelMap.put("commentPage", true);
        modelMap.put("relativePath", relativePath);
        generateBreadcrumbs();
        breadcrumbs.add(new String[]{ relativePath, "Comment"});
        return modelMap;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model) {

        model.put("pageTitle", "Comment Management");
        model.put("result", commentService.listByUserKey(user,filterForm));
        model.put("filterForm", filterForm);

        return "user/comment";
    }

    @RequestMapping(value = "/page/{page:\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String paginationMainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model, @PathVariable("page") Integer page) {

        model.put("pageTitle", "Pages "+ page+" | Comment Management");
        filterForm.setCurrentPage(page);
        model.put("result", commentService.listByUserKey(user,filterForm));
        model.put("filterForm", filterForm);

        return "user/comment";
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String processDeleteProvince(@RequestParam(value = "id") Integer id, RedirectAttributes redirectAttributes) {

        try{

            commentService.delete(id);
            redirectAttributes.addFlashAttribute("success", "");

        }catch (CustomException ex){
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:"+relativePath;
    }
}
