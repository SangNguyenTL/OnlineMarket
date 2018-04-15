package onlinemarket.controller.admin;


import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Province;
import onlinemarket.model.Rating;
import onlinemarket.service.RatingService;
import onlinemarket.util.exception.CustomException;
import onlinemarket.util.exception.rating.RatingNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/rating")
public class RatingController extends MainController {
    @Autowired
    RatingService ratingService;

    private FilterForm filterForm;

    @ModelAttribute
    private ModelMap populateAttribute(ModelMap model){
        filterForm = new FilterForm();
        relativePath = "/admin/rating";
        model.put("filterForm", filterForm);
        model.put("relativePath", relativePath);
        model.put("pathUpdate", relativePath + "/update");
        generateBreadcrumbs();
        breadcrumbs.add(new String[]{ relativePath, "Rating"});

        return model;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model) {

        model.put("pageTitle", "Rating Manager");
        model.put("path", "rating");
        model.put("result", ratingService.list(filterForm));
        model.put("filterForm", filterForm);
        return "backend/rating";
    }

    @RequestMapping(value = "/update/{id:^\\d+}", method = RequestMethod.GET)
    public String updatePage(@PathVariable("id") Integer id, ModelMap model, RedirectAttributes redirectAttributes) {

        Rating rating = ratingService.getByKey(id);
        if (rating == null) {
            redirectAttributes.addFlashAttribute("error", "Rating not found");
            return "redirect:/admin/rating";
        }

        model.put("pageTitle", "Update rating");
        model.put("subPageTitle", "Update");
        model.put("description", "Update status of rating");
        model.put("action", "update");
        model.put("rating", rating);
        model.put("pathAction", "/admin/rating/update");

        return "backend/rating-add";

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdatePage(
            @ModelAttribute("rating") @Valid Rating rating,
            BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {

        if (!result.hasErrors()) {
            try {

                ratingService.update(rating);
                redirectAttributes.addFlashAttribute("success", "");
                return "redirect:/admin/rating";

            } catch (RatingNotFoundException ex) {
                redirectAttributes.addFlashAttribute("error", ex.getMessage());
                return "redirect:/admin/rating";
            }

        }

        model.put("pageTitle", "Update rating");
        model.put("subPageTitle", "Update");
        model.put("description", "Update information of rating");
        model.put("path", "rating-add");
        model.put("action", "update");
        model.put("pathAction", "/admin/rating/update");
        model.put("rating", rating);

        return "backend/rating-add";
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String processDeleteProvince(@RequestParam(value = "id", required = true) Integer id, RedirectAttributes redirectAttributes) {

        if (id == null) {
            redirectAttributes.addFlashAttribute("error", "Program isn't get province id!");
            return "redirect:/admin/rating";
        }
        try{
            Rating ratingCheck = ratingService.getByKey(id);
            if (ratingCheck == null)
                redirectAttributes.addFlashAttribute("error", "The province isn't exist!");
            else {
                ratingService.delete(id);
                redirectAttributes.addFlashAttribute("success", "");
            }
        }catch (RatingNotFoundException ex){
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/admin/rating";
        }

        return "redirect:/admin/rating";
    }
}
