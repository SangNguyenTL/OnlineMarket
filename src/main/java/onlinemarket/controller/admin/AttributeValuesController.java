package onlinemarket.controller.admin;

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Attribute;
import onlinemarket.model.AttributeGroup;
import onlinemarket.model.AttributeValues;
import onlinemarket.model.ProductCategory;
import onlinemarket.service.AttributeGroupService;
import onlinemarket.service.AttributeService;
import onlinemarket.service.AttributeValuesService;
import onlinemarket.util.exception.attribute.AttributeNotFoundException;
import onlinemarket.util.exception.attributeGroup.AttributeGroupNotFoundException;
import onlinemarket.util.exception.attributeValues.AttributeValuesHasProductException;
import onlinemarket.util.exception.attributeValues.AttributeValuesIsExistException;
import onlinemarket.util.exception.attributeValues.AttributeValuesNotFoundException;
import onlinemarket.util.exception.productCategory.ProductCategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/product-category/{productCategoryId:^\\d++}/attribute-group/{attributeGroupId:^\\d+}/attribute/{attributeId:^\\d+}/attribute-values")
public class AttributeValuesController extends MainController {

    private FilterForm filterForm;

    private String productCategoryPath;

    private String attributeGroupPath;

    private String attributePath;

    private ProductCategory productCategory;

    private AttributeGroup attributeGroup;

    private Attribute attribute;

    @Autowired
    AttributeGroupService attributeGroupService;

    @Autowired
    AttributeService attributeService;

    @Autowired
    AttributeValuesService attributeValuesService;

    @ModelAttribute
    public ModelMap populateAttribute(@PathVariable("productCategoryId") Integer productCategoryId,
                                      @PathVariable("attributeGroupId") Integer attributeGroupId,
                                      @PathVariable("attributeId") Integer attributeId,
                                      ModelMap model) {

        productCategory = productCategoryService.getByKey(productCategoryId);
        attributeGroup = attributeGroupService.getByKey(attributeGroupId);
        attribute = attributeService.getByKey(attributeId);

        productCategoryPath = "/admin/product-category";
        attributeGroupPath = productCategoryPath + "/" + productCategoryId + "/attribute-group";
        attributePath = attributeGroupPath + "/" + attributeGroupId + "/attribute";
        relativePath = attributePath + "/" + attributeId + "/attribute-values";
        generateBreadcrumbs();
        breadcrumbs.add(new String[]{"/admin", "Admin"});
        breadcrumbs.add(new String[]{productCategoryPath, "Product category"});
        breadcrumbs.add(new String[]{attributeGroupPath, "Attribute group"});
        breadcrumbs.add(new String[]{attributePath, "Attribute"});
        breadcrumbs.add(new String[]{relativePath, "Attribute values"});

        filterForm = new FilterForm();

        model.put("productCategoryPage", true);
        model.put("filterForm", filterForm);
        model.put("productCategory", productCategory);
        model.put("attributeGroup", attributeGroup);
        model.put("attribute", attribute);
        model.put("path", "product-category");
        model.put("pathAdd", relativePath+"/add");
        model.put("relativePath", relativePath);

        return model;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(
            @ModelAttribute("filterForm") FilterForm filterForm,
            ModelMap modelMap, RedirectAttributes redirectAttributes) {

        try {

            if (productCategory == null) throw new ProductCategoryNotFoundException();
            if (attributeGroup == null) throw new AttributeGroupNotFoundException();

            modelMap.put("pageTitle", "Attribute values manager for " + attribute.getName());
            modelMap.put("result", attributeValuesService.listByAttribute(attribute, filterForm));
            modelMap.put("filterForm", filterForm);
            return "backend/attribute-values";

        } catch (ProductCategoryNotFoundException productCategoryNotFoundException) {
            redirectAttributes.addFlashAttribute("error", productCategoryNotFoundException.getMessage());
            return "redirect:" + productCategoryPath;
        } catch (AttributeGroupNotFoundException attributeGroupNotFoundException) {
            redirectAttributes.addFlashAttribute("error", attributeGroupNotFoundException.getMessage());
            return "redirect:" + attributeGroupPath;
        } catch (AttributeNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + attributePath;
        }

    }

    @RequestMapping(value = "/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePagination(
            @ModelAttribute("filterForm") FilterForm filterForm,
            @PathVariable("page") Integer page, ModelMap modelMap,
            RedirectAttributes redirectAttributes) {

        try {
            if (productCategory == null) throw new ProductCategoryNotFoundException();
            if (attributeGroup == null) throw new AttributeGroupNotFoundException();
            filterForm.setCurrentPage(page);
            modelMap.put("pageTitle", "Attribute values manager for " + attribute.getName());
            modelMap.put("result", attributeValuesService.listByAttribute(attribute, filterForm));
            modelMap.put("filterForm", filterForm);
            return "backend/attribute-values";

        } catch (ProductCategoryNotFoundException productCategoryNotFoundException) {
            redirectAttributes.addFlashAttribute("error", productCategoryNotFoundException.getMessage());
            return "redirect:" + productCategoryPath;
        } catch (AttributeGroupNotFoundException attributeGroupNotFoundException) {
            redirectAttributes.addFlashAttribute("error", attributeGroupNotFoundException.getMessage());
            return "redirect:" + attributeGroupPath;
        } catch (AttributeNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + attributePath;
        }

    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addPage(
            ModelMap model, RedirectAttributes redirectAttributes) {

        try {
            if (productCategory == null) throw new ProductCategoryNotFoundException();
            if (attributeGroup == null) throw new AttributeGroupNotFoundException();
            if (attribute == null) throw new AttributeNotFoundException();

            model.put("subPageTitle", "Add");
            model.put("description", "Add attribute value for " + attribute.getName());
            model.put("pageTitle", "Add new attribute value");
            model.put("action", "add");
            model.put("pathAction", relativePath + "/add");
            model.put("attributeValue", new AttributeValues());

            return "backend/attribute-values-add";

        } catch (ProductCategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + productCategoryPath;
        } catch (AttributeGroupNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + attributeGroupPath;
        } catch (AttributeNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + attributePath;
        }

    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddPage(
            @ModelAttribute("attributeValue") @Valid AttributeValues attributeValues,
            BindingResult result, ModelMap model,
            RedirectAttributes redirectAttributes) {

        try {
            if (productCategory == null) throw new ProductCategoryNotFoundException();
            if (attributeGroup == null) throw new AttributeGroupNotFoundException();

            if (!result.hasErrors()) {
                attributeValuesService.save(attributeValues, attribute);
                redirectAttributes.addFlashAttribute("success", true);
                return "redirect:" + relativePath;
            }

            model.put("subPageTitle", "Add");
            model.put("description", "Add attribute value for " + attribute.getName());
            model.put("pageTitle", "Add new attribute value");
            model.put("action", "add");
            model.put("pathAction", relativePath + "/add");
            model.put("attributeValue", attributeValues);

            return "backend/attribute-values-add";

        } catch (ProductCategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + productCategoryPath;
        } catch (AttributeGroupNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + attributeGroupPath;
        } catch (AttributeNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + attributePath;
        } catch (AttributeValuesIsExistException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + relativePath;
        }

    }

    @RequestMapping(value = "/update/{attributeValuesId:^\\d+}", method = RequestMethod.GET)
    public String updatePage(
            @PathVariable("attributeValuesId") int attributeValuesId,
            ModelMap model,
            RedirectAttributes redirectAttributes) {

        try {

            AttributeValues attributeValues = attributeValuesService.getByKey(attributeValuesId);
            if (productCategory == null) throw new ProductCategoryNotFoundException();
            if (attributeGroup == null) throw new AttributeGroupNotFoundException();
            if (attributeValues == null) throw new AttributeValuesNotFoundException();
            if (attribute == null) throw new AttributeNotFoundException();

            model.put("subPageTitle", "Update");
            model.put("description", "Add attribute value for " + attribute.getName());
            model.put("pageTitle", "Update attribute values");
            model.put("action", "update");
            model.put("pathAction", relativePath + "/update");
            model.put("attributeValue", attributeValues);

            return "backend/attribute-values-add";


        } catch (ProductCategoryNotFoundException e) {

            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + productCategoryPath;

        } catch (AttributeGroupNotFoundException e) {

            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + attributeGroupPath;

        } catch (AttributeValuesNotFoundException e) {

            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + relativePath;

        } catch (AttributeNotFoundException e) {

            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + attributePath;

        }


    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdatePage(
            @ModelAttribute("attributeValue") @Valid AttributeValues attributeValues,
            BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {

        try {

            if (productCategory == null) throw new ProductCategoryNotFoundException();
            if (attributeGroup == null) throw new AttributeGroupNotFoundException();
            if (attributeValues == null) throw new AttributeValuesNotFoundException();

            if(!result.hasErrors()){
                attributeValuesService.update(attributeValues, attribute);
                redirectAttributes.addFlashAttribute("success", true);
                return "redirect:" + relativePath;
            }

            model.put("subPageTitle", "Update");
            model.put("description", "Add attribute value for " + attribute.getName());
            model.put("pageTitle", "Update attribute values");
            model.put("action", "update");
            model.put("pathAction", relativePath + "/update");
            model.put("attributeValue", attributeValues);

            return "backend/attribute-values-add";


        } catch (ProductCategoryNotFoundException e) {

            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + productCategoryPath;

        } catch (AttributeGroupNotFoundException e) {

            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + attributeGroupPath;

        } catch (AttributeValuesNotFoundException e) {

            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + relativePath;

        } catch (AttributeNotFoundException e) {

            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + attributePath;

        }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String processDeleteProvince(
            @RequestParam(value = "id") Integer attributeValuesId,
            RedirectAttributes redirectAttributes) {

        try {
            attributeValuesService.delete(attributeValuesId);
            redirectAttributes.addFlashAttribute("success", "success");
        } catch (AttributeValuesHasProductException|AttributeValuesNotFoundException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
        }

        return "redirect:" + relativePath;
    }


}
