package onlinemarket.controller.admin;

import javax.validation.groups.Default;

import onlinemarket.util.exception.CustomException;
import onlinemarket.util.exception.productCategory.ProductCategoryHasAttributeGroup;
import onlinemarket.util.exception.productCategory.ProductCategoryHasEvent;
import onlinemarket.util.exception.productCategory.ProductCategoryHasProduct;
import onlinemarket.util.exception.productCategory.ProductCategoryNotFound;
import org.springframework.beans.factory.annotation.Autowired;
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

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.AttributeGroup;
import onlinemarket.model.Event;
import onlinemarket.model.Product;
import onlinemarket.model.ProductCategory;
import onlinemarket.model.other.AdvancedValidation;
import onlinemarket.result.ResultObject;
import onlinemarket.service.AttributeGroupService;
import onlinemarket.service.EventService;
import onlinemarket.service.ProductCategoryService;
import onlinemarket.service.ProductService;

@Controller
@RequestMapping("/admin/product-category")
public class ProductCategoryManagerController extends MainController {

    @Autowired
    ProductCategoryService productCategoryService;

    @Autowired
    AttributeGroupService attributeGroupService;

    @Autowired
    ProductService productService;

    @Autowired
    EventService eventService;

    FilterForm filterForm;

    @ModelAttribute
    public ModelMap populateAttribute(ModelMap model) {
        filterForm = new FilterForm();
        title = "Product category manager";
        relativePath = "/admin/product-category";

        model.put("relativePath", relativePath);
        model.put("pageTitle", title);
        model.put("filterForm", filterForm);
        model.put("productCategoryPage", true);
        return model;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String mainPage(ModelMap model) {

        model.put("path", "product-category");
        model.put("result", productCategoryService.list(filterForm));
        model.put("filterForm", filterForm);

        return "backend/product-category";
    }

    @RequestMapping(value = "/page/{page:^\\d+}", method = RequestMethod.GET)
    public String mainPagePagination(@PathVariable("page") Integer page, ModelMap model) {

        filterForm.setCurrentPage(page);
        ResultObject<ProductCategory> result = productCategoryService.list(filterForm);
        model.put("path", "productCategory");
        model.put("page", page);
        model.put("result", result);
        model.put("filterForm", filterForm);

        return "backend/productCategory";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addPage(ModelMap model) {

        model.put("subPageTitle", "Add");
        model.put("description", "Add information of productCategory");
        model.put("pageTitle", "Add new productCategory");
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
            return "redirect:" + relativePath;
        }

        model.put("subPageTitle", "Add");
        model.put("description", "Add information of productCategory");
        model.put("pageTitle", "Add new product-category");
        model.put("path", "product-category-add");
        model.put("action", "add");
        model.put("pathAction", relativePath + "/add");
        model.put("productCategory", productCategory);

        return "backend/product-category-add";
    }

    @RequestMapping(value = "/update/{id:^\\d+}", method = RequestMethod.GET)
    public String updatePage(@PathVariable("id") Integer id, ModelMap model, RedirectAttributes redirectAttributes) {

        ProductCategory productCategory = productCategoryService.getByKey(id);
        if (productCategory == null) {
            redirectAttributes.addFlashAttribute("error", "Product category not found");
            return "redirect:" + relativePath;
        }


        model.put("pageTitle", "Update Product Category");
        model.put("subPageTitle", "Update");
        model.put("description", "Update information of Product Category");
        model.put("path", "product-category-add");
        model.put("action", "update");
        model.put("pathAction", relativePath + "/update");
        model.put("productCategory", productCategory);

        return "backend/product-category-add";

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdatePage(@ModelAttribute("productCategory") @Validated(value = {Default.class,
            AdvancedValidation.CheckSlug.class}) ProductCategory productCategory,
                                    BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {


        if (!result.hasErrors()) {
            try {
                productCategoryService.update(productCategory);
            } catch (CustomException ex) {
                redirectAttributes.addFlashAttribute("error", ex.getMessage());
                return "redirect:" + relativePath;
            }

            redirectAttributes.addFlashAttribute("success", "");
            return "redirect:" + relativePath;
        }

        model.put("pageTitle", "Update product category");
        model.put("subPageTitle", "Update");
        model.put("description", "Update information of product category");
        model.put("path", "product-category-add");
        model.put("action", "update");
        model.put("pathAction", relativePath + "/update");
        model.put("productCategory", productCategory);

        return "backend/product-category-add";
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String processDeleteProductCategory(@RequestParam(value = "id", required = true) Integer id,
                                               RedirectAttributes redirectAttributes) {
        try {
            productCategoryService.delete(id);
            redirectAttributes.addFlashAttribute("success", "");
        } catch (ProductCategoryNotFound | ProductCategoryHasProduct | ProductCategoryHasEvent | ProductCategoryHasAttributeGroup ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:" + relativePath;
    }

}
