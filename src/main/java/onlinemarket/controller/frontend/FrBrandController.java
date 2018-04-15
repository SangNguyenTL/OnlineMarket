package onlinemarket.controller.frontend;

import onlinemarket.form.filter.FilterForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

@Controller
@RequestMapping("/brand")
public class FrBrandController extends FrGeneralGroup {

    private void initValue(){
        title = "Brand";
        relativePath = "/brand";
        brandPath = relativePath;
        productPath = "/product-category";
        breadcrumbs.add(new String[]{relativePath, "Brand"});
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

    @RequestMapping( value = "/{brandSlug:[\\w\\d-]+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPageProductCategory(@PathVariable("brandSlug") String brandSlug,  @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) throws NoHandlerFoundException {
        initValue();
        brand = brandService.getByDeclaration("slug",brandSlug );
        if(brand == null) throw new NoHandlerFoundException(null, null, null);
        filterForm.getPrivateGroupSearch().put("brand.slug", brand.getSlug());

        modelMap.put("title", title + "|" + brand.getName());
        modelMap.put("subTitle", brand.getName());
        productPath = relativePath + "/" + brandSlug;
        relativePath = productPath;
        breadcrumbs.add(new String[]{productPath, brand.getName()});

        return generateGeneral(filterForm, modelMap);
    }

    @RequestMapping( value = "/{brandSlug:[\\w\\d-]+}/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePaginationProductCategory(@PathVariable("brandSlug") String brandSlug, @PathVariable("page") Integer page, @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) throws NoHandlerFoundException {
        initValue();
        brand = brandService.getByDeclaration("slug",brandSlug );
        if(brand == null) throw new NoHandlerFoundException(null, null, null);
        filterForm.getPrivateGroupSearch().put("brand.slug", brand.getSlug());

        modelMap.put("title", title + "|" + brand.getName());
        modelMap.put("subTitle", brand.getName());
        productPath = relativePath + "/" + brandSlug;
        relativePath = productPath;
        breadcrumbs.add(new String[]{productPath, brand.getName()});

        filterForm.setCurrentPage(page);
        return generateGeneral(filterForm, modelMap);

    }


    @RequestMapping( value = "/{brandSlug:[\\w\\d-]+}/{productCategorySlug:[\\w\\d-]+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPageProductCategoryBrand(@PathVariable("productCategorySlug") String productCategorySlug, @PathVariable("brandSlug") String brandSlug, @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) throws NoHandlerFoundException {
        initValue();
        productCategory = productCategoryService.getByDeclaration("slug", productCategorySlug);
        brand = brandService.getByDeclaration("slug",brandSlug );
        if(brand == null) throw new NoHandlerFoundException(null, null, null);
        filterForm.getPrivateGroupSearch().put("productCategory.slug", productCategory.getSlug());
        filterForm.getPrivateGroupSearch().put("brand.slug", brand.getSlug());

        modelMap.put("title", title+"|"+brand.getSlug() + "|" + productCategory.getName()) ;
        modelMap.put("subTitle", brand.getName());
        productPath = relativePath + "/" + brandSlug;
        relativePath = productPath + "/" + productCategorySlug;
        breadcrumbs.add(new String[]{productPath, brand.getName()});
        breadcrumbs.add(new String[]{relativePath, productCategory.getName()});

        return generateGeneral(filterForm, modelMap);
    }

    @RequestMapping( value = "/{brandSlug:[\\w\\d-]+}/{productCategorySlug:[\\w\\d-]+}/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePaginationProductCategoryBrand(@PathVariable("productCategorySlug") String productCategorySlug, @PathVariable("brandSlug") String brandSlug, @PathVariable("page") Integer page, @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) throws NoHandlerFoundException {
        initValue();
        productCategory = productCategoryService.getByDeclaration("slug", productCategorySlug);
        brand = brandService.getByDeclaration("slug",brandSlug );
        if(brand == null) throw new NoHandlerFoundException(null, null, null);
        filterForm.getPrivateGroupSearch().put("productCategory.slug", productCategory.getSlug());
        filterForm.getPrivateGroupSearch().put("brand.slug", brand.getSlug());

        modelMap.put("title", title+"|"+brand.getSlug() + "|" + productCategory.getName()) ;
        modelMap.put("subTitle", brand.getName());
        productPath = relativePath + "/" + brandSlug;
        relativePath = productPath + "/" + productCategorySlug;
        breadcrumbs.add(new String[]{productPath, brand.getName()});
        breadcrumbs.add(new String[]{relativePath, productCategory.getName()});

        filterForm.setCurrentPage(page);

        return generateGeneral(filterForm, modelMap);

    }

}

