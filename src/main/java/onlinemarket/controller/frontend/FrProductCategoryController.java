package onlinemarket.controller.frontend;

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/product-category")
public class FrProductCategoryController extends MainController {

    @Autowired
    ProductService productService;

    FilterForm filterForm;

    @ModelAttribute
    public ModelMap populateAttribute(ModelMap model) {
        filterForm = new FilterForm();

        title = "Product category";
        relativePath = "/product-category";
        generateBreadcrumbs();
        breadcrumbs.add(new String[]{relativePath, "Product category"});

        filterForm.getGroupSearch().put("state", "0");
        filterForm.setOrderBy("createDate");
        filterForm.setOrder("desc");

        model.put("relativePath", relativePath);
        model.put("pageTitle", title);

        model.put("filterForm", filterForm);

        model.put("productCategoryPage", true);
        return model;
    }

    @RequestMapping( value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap){

        String orderBy = filterForm.getGroupSearch().get("orderBy");
        if(StringUtils.isNotBlank(orderBy)){
            String arrayOrderBy[] = orderBy.split("\\.");
            if(arrayOrderBy.length == 2){
                filterForm.setOrderBy(arrayOrderBy[0]);
                filterForm.setOrder(StringUtils.equalsIgnoreCase(arrayOrderBy[1], "asc") ? "asc" : "desc");
            }
        }

        modelMap.put("result", productService.list(filterForm));
        modelMap.put("filterForm", filterForm);

        return "frontend/product-category";
    }


    @RequestMapping( value = "/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePagination(@PathVariable("page") Integer page, @ModelAttribute("filterForm") FilterForm filterProduct, ModelMap modelMap){

        filterForm.getGroupSearch().remove("orderBy");

        filterForm.setCurrentPage(page);

        modelMap.put("result", productService.list(filterForm));
        modelMap.put("filterForm", filterForm);

        return "frontend/product-category";
    }
}
