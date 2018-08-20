package OnlineMarket.controller.admin;

import javax.validation.Valid;

import OnlineMarket.util.exception.attributeGroup.AttributeGroupHasAttributeException;
import OnlineMarket.util.exception.attributeGroup.AttributeGroupNotFoundException;
import OnlineMarket.util.exception.productCategory.ProductCategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import OnlineMarket.controller.MainController;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.AttributeGroup;
import OnlineMarket.model.ProductCategory;
import OnlineMarket.service.AttributeGroupService;
import OnlineMarket.service.AttributeService;

@Controller
@RequestMapping("/admin/product-category/{productCategoryId:^\\d+}/attribute-group")
public class AttributeGroupController extends MainController {

    @Autowired
    AttributeGroupService attributeGroupService;

    @Autowired
    AttributeService attributeService;

    private ProductCategory productCategory;

    private String productCategoryPath;

    @ModelAttribute
    public ModelMap modelAttribute(
            @PathVariable("productCategoryId") Integer productCategoryId,
            ModelMap model) {

        productCategory = productCategoryService.getByKey(productCategoryId);
        productCategoryPath = "/admin/product-category";
        relativePath = productCategoryPath + "/" + productCategoryId + "/attribute-group";

        generateBreadcrumbs();
        breadcrumbs.add(new String[]{"/admin", "Admin"});
        breadcrumbs.add(new String[]{productCategoryPath, "Product category"});
        breadcrumbs.add(new String[]{productCategoryPath+"/update/"+productCategory.getId(), productCategory.getName()});
        breadcrumbs.add(new String[]{relativePath, "Attribute group"});

        FilterForm filterForm = new FilterForm();
        filterForm.setSearchBy("name");

        model.put("productCategoryPage", true);
        model.put("filterForm", filterForm);
        model.put("productCategoryPath",productCategoryPath+"/update/"+productCategory.getId());
        model.put("path", "product-category");
        model.put("productCategory", productCategory);
        model.put("relativePath", relativePath);
        model.put("pathAdd", relativePath + "/add");

        return model;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(
            @ModelAttribute("filterForm") FilterForm filterForm,
            ModelMap model, RedirectAttributes redirectAttributes) {

        try {

            filterForm.setOrderBy("priority");
            filterForm.setOrder("asc");
            model.put("pageTitle", "Attribute group manager for " + productCategory.getName());
            model.put("filterForm", filterForm);
            model.put("result", attributeGroupService.listByProductCategory(productCategory, filterForm));

            return "backend/attribute-group";

        } catch (ProductCategoryNotFoundException productCategoryNotFoundException) {
            redirectAttributes.addFlashAttribute("error", productCategoryNotFoundException.getMessage());
            return "redirect:" + productCategoryPath;
        }



    }

    @RequestMapping(value = "/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePagination(
            @ModelAttribute("filterForm") FilterForm filterForm,
            @PathVariable("page") Integer page,
            ModelMap model, RedirectAttributes redirectAttributes) {

        try {

            filterForm.setCurrentPage(page);
            filterForm.setOrderBy("priority");
            filterForm.setOrder("asc");
            model.put("pageTitle", "Attribute group manager for " + productCategory.getName());
            model.put("filterForm", filterForm);
            model.put("result", attributeGroupService.listByProductCategory(productCategory, filterForm));

            return "backend/attribute-group";

        } catch (ProductCategoryNotFoundException productCategoryNotFoundException) {
            redirectAttributes.addFlashAttribute("error", productCategoryNotFoundException.getMessage());
            return "redirect:" + productCategoryPath;
        }



    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addPage(
            ModelMap model, RedirectAttributes redirectAttributes) {

         try {
             if (productCategory == null) throw new ProductCategoryNotFoundException();
             model.put("subPageTitle", "Add for " + productCategory.getName());
             model.put("description", "Add attribute group for " + productCategory.getName());
             model.put("pageTitle", "Add new attribute group");
             model.put("action", "add");
             model.put("pathAction", relativePath + "/add");
             model.put("attributeGroup", new AttributeGroup());

             return "backend/attribute-group-add";
        } catch (ProductCategoryNotFoundException e) {
             redirectAttributes.addFlashAttribute("error", e.getMessage());
             return "redirect:" + productCategoryPath;
        }

    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddPage(
            @ModelAttribute("attributeGroup") @Valid AttributeGroup attributeGroup,
            BindingResult result, ModelMap model,
            RedirectAttributes redirectAttributes) {

        try {

            if (!result.hasErrors()) {
                redirectAttributes.addFlashAttribute("success", "");
                attributeGroupService.save(attributeGroup, productCategory);
                return "redirect:" + relativePath;
            }

            model.put("subPageTitle", "Add for " + productCategory.getName());
            model.put("description", "Add attribute group for " + productCategory.getName());
            model.put("pageTitle", "Add new attribute group");
            model.put("provincePage", true);
            model.put("path", "product-category");
            model.put("action", "add");
            model.put("pathAction", relativePath + "/add");
            model.put("attributeGroup", attributeGroup);

            return "backend/attribute-group-add";

        } catch (ProductCategoryNotFoundException productCategoryNotFoundException) {
            redirectAttributes.addFlashAttribute("error", productCategoryNotFoundException.getMessage());
            return "redirect:" + productCategoryPath;
        }

    }

    @RequestMapping(value = "/update/{attributeGroupId:^\\d+}", method = RequestMethod.GET)
    public String updatePage(
            @PathVariable("attributeGroupId") int attributeGroupId,
            ModelMap model,
            RedirectAttributes redirectAttributes) {

        try {

            AttributeGroup attributeGroup = attributeGroupService.getByKey(attributeGroupId);
            if (productCategory == null) throw new ProductCategoryNotFoundException();
            if (attributeGroup == null) throw new AttributeGroupNotFoundException();

            model.put("subPageTitle", "Update for " + productCategory.getName());
            model.put("description", "Update attribute group for" + productCategory.getName());
            model.put("pageTitle", "Update attribute group");
            model.put("action", "update");
            model.put("pathAction", relativePath + "/update");
            model.put("attributeGroup", attributeGroup);

            return "backend/attribute-group-add";


        } catch (ProductCategoryNotFoundException productCategoryNotFoundException) {

            redirectAttributes.addFlashAttribute("error", productCategoryNotFoundException.getMessage());
            return "redirect:" + productCategoryPath;

        } catch (AttributeGroupNotFoundException attributeGroupNotFoundException) {

            redirectAttributes.addFlashAttribute("error", attributeGroupNotFoundException.getMessage());
            return "redirect:" + relativePath;

        }


    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdatePage(
            @ModelAttribute("attributeGroup") @Valid AttributeGroup attributeGroup,
            BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {

        try {
            if (productCategory == null) throw new ProductCategoryNotFoundException();
            if (!result.hasErrors()) {
                attributeGroupService.update(attributeGroup, productCategory);
                redirectAttributes.addFlashAttribute("success", "");
                return "redirect:" + relativePath;
            }

            model.put("subPageTitle", "Update for " + productCategory.getName());
            model.put("description", "Update attribute group for" + productCategory.getName());
            model.put("pageTitle", "Update attribute group");
            model.put("action", "update");
            model.put("pathAction", relativePath + "/update");
            model.put("attributeGroup", attributeGroup);

            return "backend/attribute-group-add";


        } catch (ProductCategoryNotFoundException productCategoryNotFoundException) {

            redirectAttributes.addFlashAttribute("error", productCategoryNotFoundException.getMessage());
            return "redirect:" + productCategoryPath;

        } catch (AttributeGroupNotFoundException attributeGroupNotFoundException) {

            redirectAttributes.addFlashAttribute("error", attributeGroupNotFoundException.getMessage());
            return "redirect:" + relativePath;

        }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String processDeleteProvince(
            @RequestParam(value = "id") Integer attributeGroupId,
            RedirectAttributes redirectAttributes) {

        try {
            attributeGroupService.delete(attributeGroupId);
            redirectAttributes.addFlashAttribute("success", "success");

        } catch (AttributeGroupNotFoundException | AttributeGroupHasAttributeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:" + relativePath;
    }

}
