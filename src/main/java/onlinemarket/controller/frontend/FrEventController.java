package onlinemarket.controller.frontend;

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Event;
import onlinemarket.model.Post;
import onlinemarket.service.EventService;
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
    EventService eventService;

    FilterForm filterForm;

    @ModelAttribute
    public ModelMap populateAttribute(ModelMap model) {

        filterForm = new FilterForm();
        filterForm.getGroupSearch();
        filterForm.setOrderBy("createDate");
        filterForm.setOrder("desc");
        filterForm.getPrivateGroupSearch().put("status", "0");

        generateBreadcrumbs();
        relativePath = "/event";
        breadcrumbs.add(new String[]{relativePath, "Event"});

        model.put("filterForm", filterForm);

        return model;
    }



    @RequestMapping(value = "/{id:\\d+}", method = RequestMethod.GET)
    public String postPage(@PathVariable("id") Integer id,  ModelMap model) throws NoHandlerFoundException {

        Event event = eventService.getByKey(id);
        if(event != null) throw new NoHandlerFoundException(null,null,null);

        breadcrumbs.add(new String[]{ relativePath, event.getName()});

        model.put("pageTile", event.getName());
        model.put("event", event);


        return "frontend/post";

    }

}
