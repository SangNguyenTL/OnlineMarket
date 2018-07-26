package onlinemarket.controller.frontend;

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Event;
import onlinemarket.model.Post;
import onlinemarket.service.EventService;
import onlinemarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

@Controller
@RequestMapping("/event")
public class FrEventController extends MainController {

    @Autowired
    ProductService productService;

    @Autowired
    EventService eventService;

    FilterForm filterForm;

    @ModelAttribute
    public ModelMap populateAttribute(ModelMap model) {

        FilterForm filterFormTopList = new FilterForm();
        filterFormTopList.getGroupSearch().put("state", "0");
        filterFormTopList.setSize(5);
        filterFormTopList.setOrder("desc");
        filterFormTopList.setOrderBy("numberOrder");
        model.put("productBestSellerList", productService.convertProductToFrProduct(productService.list(filterFormTopList).getList()));

        filterFormTopList.setOrderBy("productViewsStatistic.total");
        model.put("productBestViewing", productService.convertProductToFrProduct(productService.list(filterFormTopList).getList()));

        filterFormTopList.setOrderBy("ratingStatistic.totalScore");
        model.put("productBestRating",productService.convertProductToFrProduct(productService.list(filterFormTopList).getList()));

        filterForm = new FilterForm();
        filterForm.getGroupSearch();
        filterForm.setOrderBy("createDate");
        filterForm.setOrder("desc");
        filterForm.getPrivateGroupSearch().put("status", "0");
        title="Event list";
        generateBreadcrumbs();
        relativePath = "/event";
        breadcrumbs.add(new String[]{relativePath, "Event"});

        model.put("filterForm", filterForm);
        model.put("title", title);

        return model;
    }

    @RequestMapping( value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap){
        modelMap.put("result", eventService.list(filterForm));
        return "frontend/event-list";
    }


    @RequestMapping( value = "/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePagination(@PathVariable("page") Integer page, @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap){

        filterForm.setCurrentPage(page);
        modelMap.put("result", eventService.list(filterForm));
        return "frontend/post-list";
    }


    @RequestMapping(value = "/{id:\\d+}", method = RequestMethod.GET)
    public String postPage(@PathVariable("id") Integer id,  ModelMap model) throws NoHandlerFoundException {

        Event event = eventService.getByKey(id);
        if(event == null) throw new NoHandlerFoundException(null,null,null);

        breadcrumbs.add(new String[]{ relativePath, event.getName()});

        model.put("pageTile", event.getName());
        model.put("event", event);


        return "frontend/event";

    }

}
