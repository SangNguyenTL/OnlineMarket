package OnlineMarket.controller.frontend;

import OnlineMarket.controller.MainController;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Event;
import OnlineMarket.service.EventService;
import OnlineMarket.service.ProductService;
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

    @Override
    public void addMeta(ModelMap model) {
        generateBreadcrumbs();
        filterForm = new FilterForm();
        filterForm.getGroupSearch();
        filterForm.setOrderBy("createDate");
        filterForm.setOrder("desc");
        filterForm.getGroupSearch().put("status", "0");
        title="Event list";

        relativePath = "/event";
        breadcrumbs.add(new String[]{relativePath, "Event"});

        model.put("filterForm", filterForm);
        model.put("title", title);
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

        model.put("pageTitle", event.getName());
        model.put("event", event);


        return "frontend/event";

    }

}
