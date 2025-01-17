package OnlineMarket.controller.frontend;

import OnlineMarket.form.filter.FilterForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

@Controller
@RequestMapping("/product-category")
public class FrProductCategoryController extends FrGeneralGroup {

    private void initValue(){
        title = "Product";
        relativePath = "/product-category";
        productPath = relativePath;
        brandPage = false;
        brandPath = "/brand";
        breadcrumbs.add(new String[]{relativePath, "Product categories"});
    }

    @RequestMapping( value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap){
        initValue();
        return generateGeneral(filterForm, modelMap);
    }


    @RequestMapping( value = "/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePagination(@PathVariable("page") Integer page, @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap){
        initValue();
        filterForm.setCurrentPage(page);
        return generateGeneral(filterForm, modelMap);
    }

    @RequestMapping( value = "/{productCategorySlug:[\\w\\d-]+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPageProductCategory(@PathVariable("productCategorySlug") String productCategorySlug,  @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) throws NoHandlerFoundException {
        initValue();
        productCategory = productCategoryService.getByDeclaration("slug", productCategorySlug);
        if(productCategory == null) throw new NoHandlerFoundException(null, null, null);
        filterForm.getGroupSearch().put("productCategory.slug", productCategory.getSlug());

        brandPath = relativePath + "/" + productCategorySlug;
        relativePath = brandPath;
        title = productCategory.getName();
        breadcrumbs.add(new String[]{relativePath, title});

        return generateGeneral(filterForm, modelMap);
    }

    @RequestMapping( value = "/{productCategorySlug:[\\w\\d-]+}/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePaginationProductCategory(@PathVariable("productCategorySlug") String productCategorySlug, @PathVariable("page") Integer page, @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) throws NoHandlerFoundException {
        initValue();
        productCategory = productCategoryService.getByDeclaration("slug", productCategorySlug);
        if(productCategory == null) throw new NoHandlerFoundException(null, null, null);
        filterForm.getGroupSearch().put("productCategory.slug", productCategory.getSlug());

        brandPath = relativePath + "/" + productCategorySlug;
        relativePath = brandPath;
        title = productCategory.getName();
        breadcrumbs.add(new String[]{relativePath, title});

        filterForm.setCurrentPage(page);
        return generateGeneral(filterForm, modelMap);

    }


    @RequestMapping( value = "/{productCategorySlug:[\\w\\d-]+}/{brandSlug:[\\w\\d-]+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPageProductCategoryBrand(@PathVariable("productCategorySlug") String productCategorySlug, @PathVariable("brandSlug") String brandSlug, @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) throws NoHandlerFoundException {
        initValue();
        productCategory = productCategoryService.getByDeclaration("slug", productCategorySlug);
        brand = brandService.getByDeclaration("slug",brandSlug );
        if(brand == null) throw new NoHandlerFoundException(null, null, null);
        filterForm.getGroupSearch().put("productCategory.slug", productCategory.getSlug());
        filterForm.getGroupSearch().put("brand.slug", brand.getSlug());

        title = productCategory.getName()  + " - "+ brand.getName();

        brandPath = relativePath + "/" + productCategorySlug;
        relativePath = brandPath + "/" + brand.getSlug();
        breadcrumbs.add(new String[]{brandPath, productCategory.getName()});
        breadcrumbs.add(new String[]{relativePath, title});

        return generateGeneral(filterForm, modelMap);
    }

    @RequestMapping( value = "/{productCategorySlug:[\\w\\d-]+}/{brandSlug:[\\w\\d-]+}/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePaginationProductCategoryBrand(@PathVariable("productCategorySlug") String productCategorySlug, @PathVariable("brandSlug") String brandSlug, @PathVariable("page") Integer page, @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) throws NoHandlerFoundException {
        initValue();
        productCategory = productCategoryService.getByDeclaration("slug", productCategorySlug);
        brand = brandService.getByDeclaration("slug",brandSlug );
        if(brand == null) throw new NoHandlerFoundException(null, null, null);
        filterForm.setCurrentPage(page);
        filterForm.getGroupSearch().put("productCategory.slug", productCategory.getSlug());
        filterForm.getGroupSearch().put("brand.slug", brand.getSlug());

        title = productCategory.getName()  + " - "+ brand.getName();

        brandPath = relativePath + "/" + productCategorySlug;
        relativePath = brandPath + "/" + brand.getSlug();
        breadcrumbs.add(new String[]{brandPath, productCategory.getName()});
        breadcrumbs.add(new String[]{relativePath, title});

        return generateGeneral(filterForm, modelMap);

    }

}
