package OnlineMarket.controller.admin;


import OnlineMarket.controller.MainController;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.RatingStatistic;
import OnlineMarket.result.ResultObject;
import OnlineMarket.service.RatingStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/rating-statistic")
public class RatingStatisticController  extends MainController{

    @Autowired
    RatingStatisticService ratingStatisticService;

    @ModelAttribute
    private ModelMap modelAttribute(ModelMap model){
        FilterForm filterForm = new FilterForm();
        filterForm.setOrderBy("averageScore");
        filterForm.setOrder("desc");

        relativePath = "/admin/rating-statistic";
        model.put("filterForm", filterForm);
        model.put("relativePath", relativePath);
        model.put("reviewPage", true);
        model.put("reviewStatisticPage", true);
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
