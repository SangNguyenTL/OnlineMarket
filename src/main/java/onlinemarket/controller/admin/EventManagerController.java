package onlinemarket.controller.admin;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.groups.Default;

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
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.model.Order;
import onlinemarket.model.Product;
import onlinemarket.model.ProductCategory;
import onlinemarket.model.User;
import onlinemarket.model.other.AdvancedValidation;
import onlinemarket.service.BrandService;
import onlinemarket.service.EventService;
import onlinemarket.service.OrderService;
import onlinemarket.service.ProductCategoryService;
import onlinemarket.service.ProductService;
import onlinemarket.service.UserService;

@Controller
@RequestMapping("/admin/event")
public class EventManagerController extends MainController {
	@Autowired
	EventService eventService;

	@Autowired
	BrandService brandService;

	@Autowired
	ProductService productService;

	@Autowired
	UserService userService;

	@Autowired
	OrderService orderService;

	@Autowired
	ProductCategoryService productCategoryService;

	@ModelAttribute
	public ModelMap populateAttribute(ModelMap model) {

		model.put("filterForm", new FilterForm());
		model.put("eventPage", true);
		return model;

	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model) {

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
	public String processAddPage(@ModelAttribute("event") @Valid Event event, BindingResult result, ModelMap model,
			RedirectAttributes redirectAttributes) {

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

	@RequestMapping(value = "/update/{id:^\\d+}", method = RequestMethod.GET)
	public String updatePage(@PathVariable("id") Integer id, ModelMap model) throws NoHandlerFoundException {

		Event event = eventService.getByKey(id);
		if (event == null)
			throw new NoHandlerFoundException(null, null, null);

		model.put("pageTitle", "Update event");
		model.put("subPageTitle", "Update");
		model.put("description", "Update information of event");
		model.put("path", "event-add");
		model.put("action", "update");
		model.put("pathAction", "/admin/event/update/" + id);
		model.put("event", event);

		return "backend/event-add";

	}

	@RequestMapping(value = "/update/{id:^\\d+}", method = RequestMethod.POST)
	public String processUpdatePage(
			@ModelAttribute("event") @Validated(value = { Default.class,
					AdvancedValidation.CheckSlug.class }) Event event,
			@PathVariable("id") Integer id, BindingResult result, ModelMap model, RedirectAttributes redirectAttributes)
			throws NoHandlerFoundException {

		Event eventCheck = eventService.getByKey(id);
		if (eventCheck == null)
			throw new NoHandlerFoundException(null, null, null);

		if (!result.hasErrors()) {
			event.setUpdateDate(new Date());
			event.setCreateDate(eventCheck.getCreateDate());
			event.setUpdateDate(new Date());
			event.setPublisher(currentUser);
			eventService.update(event);
			redirectAttributes.addAttribute("success", "");
			return "redirect:admin/event/update/" + id;
		}

		model.put("pageTitle", "Update event");
		model.put("subPageTitle", "Update");
		model.put("description", "Update information of event");
		model.put("path", "event-add");
		model.put("action", "update");
		model.put("pathAction", "/admin/event/update/" + id);
		model.put("event", event);

		return "backend/event-add";
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public String processDeleteBrand(@RequestParam(value = "id", required = true) Integer id,
			RedirectAttributes redirectAttributes) {

		if (id == null) {
			redirectAttributes.addAttribute("error", "Program isn't get event id!");
			return "redirect:/admin/event";
		}
		Event eventCheck = eventService.getByKey(id);
		if (eventCheck == null) {
			redirectAttributes.addAttribute("error", "The brand isn't exist!");
		} else {
			Brand brand = brandService.getByEvent(eventCheck);
			if (brand != null)
				redirectAttributes.addAttribute("error", "The event has already had brand!");
			else {
				Product product = productService.getByEvent(eventCheck);
				if (product != null)
					redirectAttributes.addAttribute("error", "The event has already had product!");
				else {
					User user = userService.getByEvent(eventCheck);
					if (user != null)
						redirectAttributes.addAttribute("error", "The event has already had user!");
					else {
						ProductCategory proCategory = productCategoryService.getByEvent(eventCheck);
						if (proCategory != null)
							redirectAttributes.addAttribute("error", "The event has already had product category!");
						else {

							Order order = orderService.getByEvent(eventCheck);
							if (order != null)
								redirectAttributes.addAttribute("error", "The event has already had order!");
							else {
								eventService.delete(eventCheck);
								redirectAttributes.addAttribute("success", "");
							}
						}
					}
				}
			}
		}
		return "redirect:/admin/event";
	}
}
