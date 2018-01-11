package onlinemarket.controller.admin;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Event;
import onlinemarket.service.EventService;

@Controller
@RequestMapping("/admin/event")
public class EventManagerController extends MainController{
	@Autowired
	EventService eventService;
	
	@ModelAttribute
	public ModelMap populateAttribute(ModelMap model) {
		
		model.put("filterForm", new FilterForm());
		model.put("provincePage", true);
		return model;
		
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model){
		
		model.put("pageTitle", "Event Manager");
		model.put("path", "event");
		model.put("result", eventService.list(filterForm));
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
		model.put("pathAction", "/admin/event/add");
		model.put("event", new Event());
		return "backend/event-add";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String processAddPage(@ModelAttribute("event") @Valid Event event,
			BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {

		if (!result.hasErrors()) {
			event.setPublisher(currentUser);
			event.setCreateDate(new Date());
			eventService.save(event);
			redirectAttributes.addFlashAttribute("success", "");
			return "redirect:/admin/event";
			
		}
		model.put("subPageTitle", "Add");
		model.put("description", "Add information of event");
		model.put("pageTitle", "Add new event");
		model.put("path", "event-add");
		model.put("action", "add");
		model.put("pathAction", "/admin/event/add");
		model.put("event", event);
		
		return "backend/event-add";
	}
}
