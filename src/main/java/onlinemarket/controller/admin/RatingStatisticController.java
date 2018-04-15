package onlinemarket.controller.admin;


import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.RatingStatistic;
import onlinemarket.result.ResultObject;
import onlinemarket.service.RatingStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/rating-statistic")
public class RatingStatisticController  extends MainController{

    private FilterForm filterForm;

    @Autowired
    RatingStatisticService ratingStatisticService;

    @ModelAttribute
    private ModelMap populateAttribute(ModelMap model){
        filterForm = new FilterForm();
        filterForm.setOrderBy("averageScore");
        filterForm.setOrder("desc");

        relativePath = "/admin/rating-statistic";
        model.put("filterForm", filterForm);
        model.put("relativePath", relativePath);
        model.put("pathUpdate", relativePath + "/update");
        generateBreadcrumbs();
        breadcrumbs.add(new String[]{ relativePath, "Rating Statistic"});

        return model;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model) {

        model.put("pageTitle", "Rating Statistic Manager");
        model.put("path", "rating-statistic");
        ResultObject<RatingStatistic> ratingStatisticResultObject = ratingStatisticService.list(filterForm);
        model.put("result", ratingStatisticResultObject);
        model.put("filterForm", filterForm);
        return "backend/rating-statistic";
    }
}
