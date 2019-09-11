package OnlineMarket.controller.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import OnlineMarket.controller.MainController;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.service.RatingService;
import OnlineMarket.util.exception.rating.RatingNotFoundException;

@Controller
@RequestMapping("/admin/rating")
public class RatingController extends MainController {
    @Autowired
    RatingService ratingService;

    @Override
    protected void addMeta(ModelMap modelMap) {
        FilterForm filterForm = new FilterForm();
        filterForm.setSearchBy("product.name");
        relativePath = "/admin/rating";
        modelMap.put("filterForm", filterForm);
        modelMap.put("reviewPage", true);
        modelMap.put("reviewListPage", true);
        modelMap.put("relativePath", relativePath);
        generateBreadcrumbs();
        breadcrumbs.add(new String[]{ relativePath, "Rating"});
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model) {

        model.put("pageTitle", "Rating Manager");
        model.put("result", ratingService.list(filterForm));
        model.put("filterForm", filterForm);
        return "backend/rating";
    }

    @RequestMapping(value = "/page/{page:\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String paginationMainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model, @PathVariable("page") Integer page) {

        model.put("pageTitle", "Pages "+ page+" | Rating Management");
        filterForm.setCurrentPage(page);
        model.put("result", ratingService.list(filterForm));
        model.put("filterForm", filterForm);
        return "backend/rating";
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String processDeleteProvince(@RequestParam(value = "id") Integer id, RedirectAttributes redirectAttributes) {

        try{
            ratingService.delete(id);
            redirectAttributes.addFlashAttribute("success", "");

        }catch (RatingNotFoundException ex){
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/admin/rating";
        }

        return "redirect:"+relativePath;
    }
}
