package OnlineMarket.controller.admin;

import javax.validation.groups.Default;

import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.exception.productCategory.ProductCategoryHasAttributeGroupException;
import OnlineMarket.util.exception.productCategory.ProductCategoryHasEventException;
import OnlineMarket.util.exception.productCategory.ProductCategoryHasProductException;
import OnlineMarket.util.exception.productCategory.ProductCategoryNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import OnlineMarket.controller.MainController;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.ProductCategory;
import OnlineMarket.util.group.AdvancedValidation;

@Controller
@RequestMapping("/admin/product-category")
public class ProductCategoryController extends MainController {

    private FilterForm filterForm;

    @ModelAttribute
    public ModelMap modelAttribute(ModelMap model) {
        filterForm = new FilterForm();
        filterForm.setSearchBy("name");
        title = "Product category manager";
        relativePath = "/admin/product-category";

        generateBreadcrumbs();
        breadcrumbs.add(new String[]{"/admin", "Admin"});
        breadcrumbs.add(new String[]{relativePath, "Product category"});
        model.put("relativePath", relativePath);
        model.put("pageTitle", title);
        model.put("filterForm", filterForm);
        model.put("pathAdd", relativePath+"/add");
        model.put("productCategoryPage", true);
        return model;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(
            @ModelAttribute("filterForm") FilterForm filterForm,
            ModelMap model) {

        model.put("path", "product-category");
        model.put("result", productCategoryService.list(filterForm));
        model.put("filterForm", filterForm);

        return "backend/product-category";
    }

    @RequestMapping(value = "/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePagination(
            @ModelAttribute("filterForm") FilterForm filterForm,
            @PathVariable("page") Integer page, ModelMap model) {

        filterForm.setCurrentPage(page);
        model.put("path", "productCategory");
        model.put("page", page);
        model.put("result", productCategoryService.list(filterForm));
        model.put("filterForm", filterForm);

        return "backend/productCategory";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addPage(ModelMap model) {

        model.put("subPageTitle", "Add");
        model.put("description", "Add information of productCategory");
        model.put("pageTitle", "Add new product category");
        model.put("path", "product-category-add");
        model.put("action", "add");
        model.put("pathAction", relativePath + "/add");
        model.put("productCategory", new ProductCategory());

        return "backend/product-category-add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddPage(
            @ModelAttribute("productCategory") @Validated(value = {Default.class,
                    AdvancedValidation.CheckSlug.class}) ProductCategory productCategory,
            BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {

        if (!result.hasErrors()) {
            productCategoryService.save(productCategory);
            redirectAttributes.addFlashAttribute("success", "");
            return "redirect:" + relativePath+"/update/"+productCategory.getId();
        }

        model.put("subPageTitle", "Add");
        model.put("description", "Add information of product category");
        model.put("pageTitle", "Add new product-category");
        model.put("path", "product-category-add");
        model.put("action", "add");
        model.put("pathAction", relativePath + "/add");
        model.put("productCategory", productCategory);

        return "backend/product-category-add";
    }

    @RequestMapping(value = "/update/{productCategoryId:^\\d+}", method = RequestMethod.GET)
    public String updatePage(@PathVariable("productCategoryId") Integer productCategoryId, ModelMap model, RedirectAttributes redirectAttributes) {

        try{
            ProductCategory productCategory = productCategoryService.getByKey(productCategoryId);
            if (productCategory == null) throw new ProductCategoryNotFoundException();
            model.put("pageTitle", "Update Product Category");
            model.put("subPageTitle", "Update");
            model.put("description", "Update information of Product Category");
            model.put("path", "product-category-add");
            model.put("action", "update");
            model.put("pathAction", relativePath + "/update");
            model.put("productCategory", productCategory);

            return "backend/product-category-add";
        }catch (ProductCategoryNotFoundException ex){
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:" + relativePath;
        }

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdatePage(@ModelAttribute("productCategory") @Validated(value = {Default.class,
            AdvancedValidation.CheckSlug.class}) ProductCategory productCategory,
                                    BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {

        try {
            if (!result.hasErrors()) {
                productCategoryService.update(productCategory);
                redirectAttributes.addFlashAttribute("success", true);
                return "redirect:" + relativePath+"/update/"+productCategory.getId();
            }
            model.put("pageTitle", "Update product category");
            model.put("subPageTitle", "Update");
            model.put("description", "Update information of product category");
            model.put("path", "product-category-add");
            model.put("action", "update");
            model.put("pathAction", relativePath + "/update");
            model.put("productCategory", productCategory);

            return "backend/product-category-add";

        } catch (CustomException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:" + relativePath;
        }

    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String processDeleteProductCategory(@RequestParam(value = "id") Integer id,
                                               RedirectAttributes redirectAttributes) {
        try {
            productCategoryService.delete(id);
            redirectAttributes.addFlashAttribute("success", "");
        } catch (ProductCategoryNotFoundException | ProductCategoryHasProductException | ProductCategoryHasEventException | ProductCategoryHasAttributeGroupException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:" + relativePath;
    }

}
