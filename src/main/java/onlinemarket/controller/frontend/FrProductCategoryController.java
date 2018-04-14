package onlinemarket.controller.frontend;

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.form.filter.FilterProduct;
import onlinemarket.service.ProductService;
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

    FilterProduct filterProduct;

    @ModelAttribute
    public ModelMap populateAttribute(ModelMap model) {
        filterForm = new FilterForm();
        filterProduct = new FilterProduct(filterForm);

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
        model.put("filterProduct", filterProduct);

        model.put("productCategoryPage", true);
        return model;
    }

    @RequestMapping( value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@ModelAttribute("filterProduct") FilterProduct filterProduct, ModelMap modelMap){

        modelMap.put("result", productService.list(filterProduct.getFilterForm()));
        modelMap.put("filterProduct", filterProduct);
        modelMap.put("filterForm", filterProduct.getFilterForm());

        return "frontend/product-category";
    }


    @RequestMapping( value = "/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePagination(@PathVariable("page") Integer page, @ModelAttribute("filterProduct") FilterProduct filterProduct, ModelMap modelMap){

        filterForm.setCurrentPage(page);

        modelMap.put("result", productService.list(filterProduct.getFilterForm()));
        modelMap.put("filterProduct", filterProduct);
        modelMap.put("filterForm", filterProduct.getFilterForm());

        return "frontend/product-category";
    }
}
