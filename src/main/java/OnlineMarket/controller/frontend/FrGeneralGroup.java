package OnlineMarket.controller.frontend;

import OnlineMarket.controller.MainController;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Brand;
import OnlineMarket.model.ProductCategory;
import OnlineMarket.service.BrandService;
import OnlineMarket.service.ProductCategoryService;
import OnlineMarket.service.ProductService;
import OnlineMarket.util.other.ProductStatus;
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

    protected Boolean brandPage;

    @Override
    public void addMeta(ModelMap model) {

        filterForm = new FilterForm();

        filterForm.getGroupSearch().put("state", ProductStatus.INSTOCK.getId().toString());
        filterForm.setOrderBy("releaseDate");
        filterForm.setOrder("desc");

        brand = null;
        productCategory = null;

        model.put("filterForm", filterForm);
    }


    protected String generateGeneral(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) {

        modelMap.put("result", productService.frontendProductResultObject( productService.list(filterForm)));
        modelMap.put("productCategory", productCategory);
        modelMap.put("brand", brand);
        modelMap.put("brandPath", brandPath);
        modelMap.put("productPath", productPath);
        modelMap.put("relativePath", relativePath);

        modelMap.put("pageTitle", title);
        modelMap.put("title", title);
        modelMap.put("brandPage", brandPage);
        modelMap.put("filterForm", filterForm);

        return template;
    }
}
