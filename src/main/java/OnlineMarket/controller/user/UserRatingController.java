package OnlineMarket.controller.user;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.service.RatingService;
import OnlineMarket.util.exception.rating.RatingNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user/{userId:\\d+}/rating")
public class UserRatingController extends UserControllerInterface {

    @Autowired
    private RatingService ratingService;

    @Override
    public ModelMap modelMap(ModelMap modelMap) {
        FilterForm filterForm = new FilterForm();
        filterForm.setSearchBy("product.name");
        relativePath = userProfilePath+"/rating";
        modelMap.put("filterForm", filterForm);
        modelMap.put("ratingPage", true);
        modelMap.put("relativePath", relativePath);
        generateBreadcrumbs();
        breadcrumbs.add(new String[]{ relativePath, "Rating"});
        return modelMap;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model) {

        model.put("pageTitle", "Rating Management");
        model.put("result", ratingService.listByUserKey(user,filterForm));
        model.put("filterForm", filterForm);

        return "user/rating";
    }

    @RequestMapping(value = "/page/{page:\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String paginationMainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model, @PathVariable("page") Integer page) {

        model.put("pageTitle", "Pages "+ page+" | Rating Management");
        filterForm.setCurrentPage(page);
        model.put("result", ratingService.listByUserKey(user,filterForm));
        model.put("filterForm", filterForm);

        return "user/rating";
    }


    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String processDeleteProvince(@RequestParam(value = "id") Integer id, RedirectAttributes redirectAttributes) {

        try{
            ratingService.delete(id);
            redirectAttributes.addFlashAttribute("success", "");

        }catch (RatingNotFoundException ex){
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:"+relativePath;
    }
}
