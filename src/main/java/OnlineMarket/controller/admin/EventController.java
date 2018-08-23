package OnlineMarket.controller.admin;


import OnlineMarket.util.group.AdvancedValidation;
import OnlineMarket.util.exception.event.EventNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import OnlineMarket.controller.MainController;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Event;
import OnlineMarket.service.EventService;

import javax.validation.groups.Default;

@Controller
@RequestMapping("/admin/event")
public class EventController extends MainController {

    @Autowired
    EventService eventService;

    protected FilterForm filterForm;

    @ModelAttribute
    public ModelMap modelAttribute(ModelMap model) {
        filterForm = new FilterForm();

        relativePath = "/admin/event";
        generateBreadcrumbs();
        breadcrumbs.add(new String[]{relativePath, "Event"});
        model.put("relativePath", relativePath);
        model.put("filterForm", filterForm);
        model.put("eventPage", true);
        model.put("pathAdd", relativePath + "/add");
        return model;

    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model) {

        model.put("pageTitle", "Event Manager");
        model.put("path", "event");
        model.put("result", eventService.list(filterForm));
        model.put("filterForm", filterForm);
        return "backend/event";
    }

    @RequestMapping(value = "/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePagination(@ModelAttribute("filterForm") FilterForm filterForm, @PathVariable("page") Integer page, ModelMap model) {

        filterForm.setCurrentPage(page);

        model.put("result", eventService.list(filterForm));
        model.put("page", page);
        model.put("pageTitle", "Brand Manager");
        model.put("path", "brand");
        model.put("filterForm", filterForm);

        return "backend/event";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addPage(ModelMap model) {

        model.put("subPageTitle", "Add");
        model.put("description", "Add information of event");
        model.put("pageTitle", "Add new event");
        model.put("provincePage", true);
        model.put("path", "event-add");
        model.put("action", "add");
        model.put("pathAction", relativePath + "/add");
        model.put("event", new Event());
        return "backend/event-add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddPage(@Validated(value = {Default.class, AdvancedValidation.class}) @ModelAttribute("event") Event event, BindingResult result, ModelMap model,
                                 RedirectAttributes redirectAttributes) {

        if (!result.hasErrors()) {
            eventService.save(event, currentUser);
            redirectAttributes.addFlashAttribute("success", "");
            return "redirect:" + relativePath+"/update/"+event.getId();
        }

        model.put("subPageTitle", "Add");
        model.put("description", "Add information of event");
        model.put("pageTitle", "Add new event");
        model.put("path", "event-add");
        model.put("action", "add");
        model.put("pathAction", relativePath + "/add");
        model.put("event", event);

        return "backend/event-add";
    }

    @RequestMapping(value = "/update/{id:^\\d+}", method = RequestMethod.GET)
    public String updatePage(@PathVariable("id") Integer id, ModelMap model, RedirectAttributes redirectAttributes) {

        try {
            Event event = eventService.getByKey(id);
            if (event == null)
                throw new EventNotFoundException();

            model.put("pageTitle", "Update event");
            model.put("subPageTitle", "Update");
            model.put("description", "Update information of event");
            model.put("path", "event-add");
            model.put("action", "update");
            model.put("pathAction", relativePath + "/update");
            model.put("event", event);

            return "backend/event-add";

        } catch (EventNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return  "redirect:"+relativePath;
        }

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdatePage(
            @Validated(value = {Default.class, AdvancedValidation.class})
            @ModelAttribute("event") Event event,
            BindingResult result, ModelMap model, RedirectAttributes redirectAttributes){

        try {

           if(!result.hasErrors()){
               eventService.update(event, currentUser);
               redirectAttributes.addFlashAttribute("success", true);
               return "redirect:" + relativePath+"/update/"+event.getId();
           }

            model.put("pageTitle", "Update event");
            model.put("subPageTitle", "Update");
            model.put("description", "Update information of event");
            model.put("path", "event-add");
            model.put("action", "update");
            model.put("pathAction", relativePath + "/update");
            model.put("event", event);

            return "backend/event-add";

        } catch (EventNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return  "redirect:"+relativePath;
        }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String processDeleteBrand(@RequestParam(value = "id") Integer id,
                                     RedirectAttributes redirectAttributes) {
        try {
            eventService.delete(id);
        } catch (EventNotFoundException e) {
           redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:" + relativePath;
    }
}
