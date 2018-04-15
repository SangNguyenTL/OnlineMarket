package onlinemarket.controller.frontend;

import onlinemarket.controller.MainController;
import onlinemarket.dao.ProductCategoryDao;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Brand;
import onlinemarket.model.ProductCategory;
import onlinemarket.service.BrandService;
import onlinemarket.service.ProductCategoryService;
import onlinemarket.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

@Controller
@RequestMapping("/product-category")
public class FrProductCategoryController extends MainController {

    @Autowired
    ProductService productService;

    @Autowired
    ProductCategoryService productCategoryService;

    @Autowired
    BrandService brandService;

    private FilterForm filterForm;

    private ProductCategory productCategory;

    private Brand brand;

    @ModelAttribute
    public ModelMap populateAttribute(ModelMap model) {
        filterForm = new FilterForm();

        title = "Product category";
        relativePath = "/product-category";
        generateBreadcrumbs();
        breadcrumbs.add(new String[]{relativePath, "Product category"});

        filterForm.getPrivateGroupSearch().put("state", "0");
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

        return generateGeneral(filterForm, modelMap);
    }


    @RequestMapping( value = "/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePagination(@PathVariable("page") Integer page, @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap){
        filterForm.setCurrentPage(page);
        return generateGeneral(filterForm, modelMap);
    }

    @RequestMapping( value = "/{productCategorySlug:[\\w\\d-]+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPageProductCategory(@PathVariable("productCategorySlug") String productCategorySlug,  @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) throws NoHandlerFoundException {
        productCategory = productCategoryService.getByDeclaration("slug", productCategorySlug);
        if(productCategory == null) throw new NoHandlerFoundException(null, null, null);
        filterForm.getPrivateGroupSearch().put("productCategory.slug", productCategory.getSlug());

        modelMap.put("title", title + "|" + productCategory.getName());
        modelMap.put("subTitle", productCategory.getName());
        breadcrumbs.add(new String[]{relativePath + "/" + productCategorySlug, productCategory.getName()});

        return generateGeneral(filterForm, modelMap);
    }

    @RequestMapping( value = "/{productCategorySlug:[\\w\\d-]+}/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePaginationProductCategory(@PathVariable("productCategorySlug") String productCategorySlug, @PathVariable("page") Integer page, @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) throws NoHandlerFoundException {

        productCategory = productCategoryService.getByDeclaration("slug", productCategorySlug);
        if(productCategory == null) throw new NoHandlerFoundException(null, null, null);
        filterForm.getPrivateGroupSearch().put("productCategory.slug", productCategory.getSlug());

        modelMap.put("title", title + "|" + productCategory.getName());
        modelMap.put("subTitle", productCategory.getName());
        breadcrumbs.add(new String[]{relativePath + "/" + productCategorySlug, productCategory.getName()});

        filterForm.setCurrentPage(page);
        return generateGeneral(filterForm, modelMap);

    }


    @RequestMapping( value = "/{productCategorySlug:[\\w\\d-]+}/{brandSlug:[\\w\\d-]+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPageProductCategoryBrand(@PathVariable("productCategorySlug") String productCategorySlug, @PathVariable("brandSlug") String brandSlug, @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) throws NoHandlerFoundException {
        productCategory = productCategoryService.getByDeclaration("slug", productCategorySlug);
        brand = brandService.getByDeclaration("slug",brandSlug );
        if(brand == null) throw new NoHandlerFoundException(null, null, null);
        filterForm.getPrivateGroupSearch().put("productCategory.slug", productCategory.getSlug());
        filterForm.getPrivateGroupSearch().put("brand.slug", brand.getSlug());

        modelMap.put("title", title + "|" + productCategory.getName()+"|"+brand.getSlug()) ;
        modelMap.put("subTitle", brand.getName());
        breadcrumbs.add(new String[]{relativePath + "/" + productCategorySlug, productCategory.getName()});
        breadcrumbs.add(new String[]{relativePath + "/" + productCategorySlug + "/" + brandSlug, brand.getName()});

        return generateGeneral(filterForm, modelMap);
    }

    @RequestMapping( value = "/{productCategorySlug:[\\w\\d-]+}/{brandSlug:[\\w\\d-]+}/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePaginationProductCategoryBrand(@PathVariable("productCategorySlug") String productCategorySlug, @PathVariable("brandSlug") String brandSlug, @PathVariable("page") Integer page, @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) throws NoHandlerFoundException {

        productCategory = productCategoryService.getByDeclaration("slug", productCategorySlug);
        brand = brandService.getByDeclaration("slug",brandSlug );
        if(brand == null) throw new NoHandlerFoundException(null, null, null);
        filterForm.setCurrentPage(page);
        filterForm.getPrivateGroupSearch().put("productCategory.slug", productCategory.getSlug());
        filterForm.getPrivateGroupSearch().put("brand.slug", brand.getSlug());

        modelMap.put("title", title + "|" + productCategory.getName()+"|"+brand.getSlug()) ;
        modelMap.put("subTitle", brand.getName());
        breadcrumbs.add(new String[]{relativePath + "/" + productCategorySlug, productCategory.getName()});
        breadcrumbs.add(new String[]{relativePath + "/" + productCategorySlug + "/" + brandSlug, brand.getName()});

        return generateGeneral(filterForm, modelMap);

    }

    private String generateGeneral(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) {
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
        modelMap.put("productCategory", productCategory);
        modelMap.put("brand", brand);
        return "frontend/product-category";
    }
}
