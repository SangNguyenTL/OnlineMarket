package OnlineMarket.controller.frontend;


import OnlineMarket.controller.MainController;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.service.ProductService;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    ProductService productService;

    @Override
    protected void addMeta(ModelMap modelMap) {
        FilterForm filterForm = new FilterForm();

        filterForm.getGroupSearch().put("state", "0");
        filterForm.setOrderBy("releaseDate");
        filterForm.setOrder("desc");
        relativePath = "/search";

        modelMap.put("relativePath", relativePath);
        modelMap.put("filterForm", filterForm);
        modelMap.put("pageTitle", "Search");
        generateBreadcrumbs();

        breadcrumbs.add(new String[]{relativePath, "Search"});
    }

    @RequestMapping( value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap){
        processFilterForm(filterForm);
        modelMap.put("result", productService.frontendProductResultObject(productService.list(filterForm)));
        return "frontend/search";
    }


    @RequestMapping( value = "/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePagination(@PathVariable("page") Integer page, @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap){
        processFilterForm(filterForm);
        filterForm.setCurrentPage(page);
        modelMap.put("pageTitle", "Pages "+page+" | Search");
        modelMap.put("result", productService.frontendProductResultObject(productService.list(filterForm)));

        return "frontend/search";
    }

    void processFilterForm(FilterForm filterForm){
        String orderBy = filterForm.getGroupSearch().get("orderBy");
        if(StringUtils.isNotBlank(orderBy)){
            String arrayOrderBy[] = orderBy.split("\\.");
            if(arrayOrderBy.length == 2){
                filterForm.setOrderBy(arrayOrderBy[0]);
                filterForm.setOrder(StringUtils.equalsIgnoreCase(arrayOrderBy[1], "asc") ? "asc" : "desc");
            }else
            if(arrayOrderBy.length == 3){
                filterForm.setOrderBy(arrayOrderBy[0]+"."+arrayOrderBy[1]);
                filterForm.setOrder(StringUtils.equalsIgnoreCase(arrayOrderBy[1], "asc") ? "asc" : "desc");
            }
        }
    }
}

