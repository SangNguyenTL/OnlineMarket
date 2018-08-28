package OnlineMarket.controller.user;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Order;
import OnlineMarket.service.OrderService;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.other.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user/{userId:\\d+}/order")
public class UserOrderController extends UserControllerInterface {

    @Autowired
    OrderService orderService;

    @Override
    public ModelMap modelMap(ModelMap modelMap) {
        modelMap.put("pageTitle", user.getDisplayName() + " | User Order");
        relativePath = userProfilePath+"/order";
        FilterForm filterForm = new FilterForm();
        filterForm.setSearchBy("id");
        modelMap.put("filterForm", filterForm);
        modelMap.put("orderPage", true);
        modelMap.put("relativePath", relativePath);
        generateBreadcrumbs();
        breadcrumbs.add(new String[]{ relativePath, "Order"});
        return modelMap;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model) {

        model.put("result", orderService.listByUserKey(user, filterForm));
        model.put("filterForm", filterForm);
        return "user/order";
    }

    @RequestMapping(value = "/page/{page:\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String paginationMainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap model, @PathVariable("page") Integer page) {

        filterForm.setCurrentPage(page);
        model.put("result", orderService.listByUserKey(user, filterForm));
        model.put("filterForm", filterForm);
        return "user/order";
    }


    @RequestMapping(value = "/view/{id:\\d+}", method = RequestMethod.GET)
    public String updatePage(@PathVariable("id") Integer id, ModelMap modelMap, RedirectAttributes redirectAttributes) {
        Order order = orderService.getByKey(id);
        if(order == null){
            redirectAttributes.addFlashAttribute("error", "Invoice not found.");
            return "redirect:" + relativePath;
        }
        modelMap.put("order", order);

        return "user/invoice";
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String processDeleteProvince(@RequestParam(value = "id") Integer id, RedirectAttributes redirectAttributes) {

        try{
            orderService.delete(id);
            redirectAttributes.addFlashAttribute("success", "");

        }catch (CustomException ex){
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:"+relativePath;
    }

}
