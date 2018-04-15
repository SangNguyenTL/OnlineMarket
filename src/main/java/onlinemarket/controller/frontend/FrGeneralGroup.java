package onlinemarket.controller.frontend;

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Brand;
import onlinemarket.model.ProductCategory;
import onlinemarket.service.BrandService;
import onlinemarket.service.ProductCategoryService;
import onlinemarket.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;

public class FrGeneralGroup extends MainController {

    @Autowired
    ProductService productService;

    @Autowired
    ProductCategoryService productCategoryService;

    @Autowired
    BrandService brandService;

    protected FilterForm filterForm;

    protected ProductCategory productCategory;

    protected Brand brand;

    protected String template = "frontend/product-category";

    protected String brandPath;

    protected String productPath;

    protected String title;

    protected String relativePath;

    @ModelAttribute
    public ModelMap populateAttribute(ModelMap model) {
        filterForm = new FilterForm();

        FilterForm filterForm1 = new FilterForm();
        filterForm1.setSize(5);
        filterForm1.setOrder("desc");
        filterForm1.setOrderBy("numberOrder");
        model.put("productBestSellerList",productService.list(filterForm1).getList());

        FilterForm filterForm2 = new FilterForm();
        filterForm2.setSize(5);
        filterForm2.setOrder("desc");
        filterForm2.setOrderBy("productViewsStatistic.total");
        model.put("productBestViewing",productService.list(filterForm2).getList());

        FilterForm filterForm3 = new FilterForm();
        filterForm3.setSize(5);
        filterForm3.setOrder("desc");
        filterForm3.setOrderBy("ratingStatistic.totalScore");
        model.put("productBestRating",productService.list(filterForm3).getList());

        filterForm.getPrivateGroupSearch().put("state", "0");
        filterForm.setOrderBy("createDate");
        filterForm.setOrder("desc");

        generateBreadcrumbs();

        return model;
    }


    protected String generateGeneral(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) {
        String orderBy = filterForm.getGroupSearch().get("orderBy");
        if(StringUtils.isNotBlank(orderBy)){
            String arrayOrderBy[] = orderBy.split("\\.");
            if(arrayOrderBy.length == 2){
                filterForm.setOrderBy(arrayOrderBy[0]);
                filterForm.setOrder(StringUtils.equalsIgnoreCase(arrayOrderBy[1], "asc") ? "asc" : "desc");
            }
        }


        modelMap.put("result", productService.list(filterForm));
        modelMap.put("productCategory", productCategory);
        modelMap.put("brand", brand);
        modelMap.put("brandPath", brandPath);
        modelMap.put("productPath", productPath);
        modelMap.put("relativePath", relativePath);

        modelMap.put("pageTitle", title);
        modelMap.put("filterForm", filterForm);

        return template;
    }
}
