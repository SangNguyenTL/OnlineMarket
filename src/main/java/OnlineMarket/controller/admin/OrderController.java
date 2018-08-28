package OnlineMarket.controller.admin;


import OnlineMarket.controller.MainController;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Order;
import OnlineMarket.service.OrderService;
import OnlineMarket.service.RatingService;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.exception.rating.RatingNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/order")
public class OrderController extends MainController {
    @Autowired
    OrderService orderService;

    @Override
    protected void addMeta(ModelMap modelMap) {
        FilterForm filterForm = new FilterForm();
        filterForm.setSearchBy("user.firstName");
        relativePath = "/admin/order";
        modelMap.put("filterForm", filterForm);
        modelMap.put("orderPage", true);
        modelMap.put("relativePath", relativePath);
        generateBreadcrumbs();
        breadcrumbs.add(new String[]{ relativePath, "Order"});
        modelMap.put("countOrder", orderService.count());
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model) {

        model.put("pageTitle", "Order Manager");
        model.put("result", orderService.list(filterForm));
        model.put("filterForm", filterForm);
        return "backend/order";
    }

    @RequestMapping(value = "/page/{page:\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String paginationMainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model, @PathVariable("page") Integer page) {

        model.put("pageTitle", "Pages "+ page+" | Order Management");
        filterForm.setCurrentPage(page);
        model.put("result", orderService.list(filterForm));
        model.put("filterForm", filterForm);
        return "backend/order";
    }


    @RequestMapping(value = "/update/{id:\\d+}", method = RequestMethod.GET)
    public String updatePage(@PathVariable("id") Integer id, ModelMap modelMap, RedirectAttributes redirectAttributes) {
        Order order = orderService.getByKey(id);
        if(order == null){
            redirectAttributes.addFlashAttribute("error", "Invoice not found.");
            return "redirect:" + relativePath;
        }
        modelMap.put("order", order);

        return "backend/invoice";
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String processDeleteProvince(@RequestParam(value = "id") Integer id, RedirectAttributes redirectAttributes) {

        try{
            orderService.delete(id);
            redirectAttributes.addFlashAttribute("success", "");

        }catch (CustomException ex){
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/admin/order";
        }

        return "redirect:"+relativePath;
    }
}
