package OnlineMarket.controller.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.util.exception.CustomException;

@Controller
@RequestMapping("/user/{userId:\\d+}/notification")
public class UserNotificationController extends UserProfileController {

    @Override
    public ModelMap modelMap(ModelMap modelMap) {
        FilterForm filterForm = new FilterForm();
        filterForm.setSearchBy("post.name");
        filterForm.setOrderBy("createDate");
        filterForm.setOrder("DESC");
        relativePath = userProfilePath+"/notification";
        title = "Notification management";
        modelMap.put("filterForm", filterForm);
        modelMap.put("notification", true);
        modelMap.put("relativePath", relativePath);
        generateBreadcrumbs();
        breadcrumbs.add(new String[]{ relativePath, title});
        return modelMap;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap,
                           HttpServletRequest request) throws NoHandlerFoundException {
        try {
            modelMap.put("pageTitle", title);
            modelMap.put("result", notificationService.listByUser(user.getId(), filterForm));
        } catch (CustomException e) {
            throw new NoHandlerFoundException(request.getMethod(),request.getRequestURL().toString(), null);
        }

        return "user/notification";
    }

    @RequestMapping(value = "/page/{page:\\d+]}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePagination(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap,
                           HttpServletRequest request,
                           @PathVariable("page") Integer page) throws NoHandlerFoundException {
        try {
            modelMap.put("pageTitle", "Pages "+page+" | "+title);
            filterForm.setCurrentPage(page);
            modelMap.put("result", notificationService.listByUser(user.getId(), filterForm));
        } catch (CustomException e) {
            throw new NoHandlerFoundException(request.getMethod(),request.getRequestURL().toString(), null);
        }

        return "user/notification";
    }


    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String processDeleteProvince(@RequestParam(value = "id") Integer id, RedirectAttributes redirectAttributes) {

        try{
            notificationService.delete(id);
            redirectAttributes.addFlashAttribute("success", "");

        }catch (CustomException ex){
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:"+relativePath;
    }
}
