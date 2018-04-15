package onlinemarket.controller.frontend;


import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/search")
public class FrSearchController extends MainController {

    private FilterForm filterForm;

    @Autowired
    ProductService productService;

    @ModelAttribute
    public ModelMap populateAttribute(ModelMap model) {
        filterForm = new FilterForm();

        filterForm.getPrivateGroupSearch().put("state", "0");
        filterForm.setOrderBy("releaseDate");
        filterForm.setOrder("desc");
        relativePath = "/search";

        model.put("relativePath", relativePath);
        model.put("filterForm", filterForm);

        generateBreadcrumbs();
        breadcrumbs.add(new String[]{relativePath, "Search"});

        return model;
    }
    @RequestMapping( value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap){

        modelMap.put("result", productService.list(filterForm));

        return "frontend/search";
    }


    @RequestMapping( value = "/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePagination(@PathVariable("page") Integer page, @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap){

        filterForm.setCurrentPage(page);
        modelMap.put("result", productService.list(filterForm));

        return "frontend/search";
    }

}

