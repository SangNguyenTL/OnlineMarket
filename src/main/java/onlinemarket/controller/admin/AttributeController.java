package onlinemarket.controller.admin;

import javax.validation.Valid;

import onlinemarket.util.exception.attribute.AttributeHasAttributeValuesException;
import onlinemarket.util.exception.attribute.AttributeNotFoundException;
import onlinemarket.util.exception.attributeGroup.AttributeGroupNotFoundException;
import onlinemarket.util.exception.productCategory.ProductCategoryNotFoundException;
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

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Attribute;
import onlinemarket.model.AttributeGroup;
import onlinemarket.model.ProductCategory;
import onlinemarket.service.AttributeGroupService;
import onlinemarket.service.AttributeService;

@Controller
@RequestMapping("/admin/product-category/{productCategoryId:^\\d++}/attribute-group/{attributeGroupId:^\\d+}/attribute")
public class AttributeController extends MainController {

    @Autowired
    AttributeGroupService attributeGroupService;

    @Autowired
    AttributeService attributeService;

    private ProductCategory productCategory;

    private AttributeGroup attributeGroup;

    private String productCategoryPath;

    private String attributeGroupPath;

    private FilterForm filterForm;

    @ModelAttribute
    public ModelMap populateAttribute(@PathVariable("productCategoryId") Integer productCategoryId,
                                      @PathVariable("attributeGroupId") Integer attributeGroupId,
                                      ModelMap model) {

        productCategory = productCategoryService.getByKey(productCategoryId);
        attributeGroup = attributeGroupService.getByKey(attributeGroupId);
        productCategoryPath = "/admin/product-category";
        attributeGroupPath = productCategoryPath + "/" + productCategoryId + "/attribute-group";
        relativePath = attributeGroupPath + "/" + attributeGroupId + "/attribute";
        generateBreadcrumbs();
        breadcrumbs.add(new String[]{"/admin", "Admin"});
        breadcrumbs.add(new String[]{productCategoryPath, "Product category"});
        breadcrumbs.add(new String[]{attributeGroupPath, "Attribute group"});
        breadcrumbs.add(new String[]{relativePath, "Attribute"});

        filterForm = new FilterForm();

        model.put("productCategoryPage", true);
        model.put("filterForm", filterForm);
        model.put("productCategory", productCategory);
        model.put("attributeGroup", attributeGroup);
        model.put("path", "product-category");
        model.put("relativePath", relativePath);
        model.put("pathAdd", relativePath + "/add");

        return model;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(
            @ModelAttribute("filterForm") FilterForm filterForm,
            ModelMap model,
            RedirectAttributes redirectAttributes) {

        try {

            if (productCategory == null) throw new ProductCategoryNotFoundException();

            filterForm.setOrderBy("priority");
            filterForm.setOrder("asc");

            model.put("pageTitle", "Attribute Manager for " + attributeGroup.getName());
            model.put("result", attributeService.listByAttributeGroup(attributeGroup, filterForm));
            model.put("filterForm", filterForm);
            return "backend/attribute";

        } catch (ProductCategoryNotFoundException productCategoryNotFoundException) {
            redirectAttributes.addFlashAttribute("error", productCategoryNotFoundException.getMessage());
            return "redirect:" + productCategoryPath;
        } catch (AttributeGroupNotFoundException attributeGroupNotFoundException) {
            redirectAttributes.addFlashAttribute("error", attributeGroupNotFoundException.getMessage());
            return "redirect:" + attributeGroupPath;
        }

    }

    @RequestMapping(value = "/page/{page:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPagePagination(
            @ModelAttribute("filterForm") FilterForm filterForm,
            @PathVariable("page") Integer page, ModelMap model,
            RedirectAttributes redirectAttributes) {

        try {

            if (productCategory == null) throw new ProductCategoryNotFoundException();

            filterForm.setCurrentPage(page);
            filterForm.setOrderBy("priority");
            filterForm.setOrder("asc");

            model.put("pageTitle", "Attribute Manager for " + attributeGroup.getName());
            model.put("result", attributeService.listByAttributeGroup(attributeGroup, filterForm));
            model.put("filterForm", filterForm);
            return "backend/attribute";

        } catch (ProductCategoryNotFoundException productCategoryNotFoundException) {
            redirectAttributes.addFlashAttribute("error", productCategoryNotFoundException.getMessage());
            return "redirect:" + productCategoryPath;
        } catch (AttributeGroupNotFoundException attributeGroupNotFoundException) {
            redirectAttributes.addFlashAttribute("error", attributeGroupNotFoundException.getMessage());
            return "redirect:" + attributeGroupPath;
        }

    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addPage(ModelMap model, RedirectAttributes redirectAttributes) {

        try {
            if (productCategory == null) throw new ProductCategoryNotFoundException();
            if (attributeGroup == null) throw new AttributeGroupNotFoundException();

            model.put("subPageTitle", "Add");
            model.put("description", "Add attribute for " + attributeGroup.getName());
            model.put("pageTitle", "Add new attribute");
            model.put("action", "add");
            model.put("pathAction", relativePath + "/add");
            model.put("attribute", new Attribute());

            return "backend/attribute-add";

        } catch (ProductCategoryNotFoundException productCategoryNotFoundException) {
            redirectAttributes.addFlashAttribute("error", productCategoryNotFoundException.getMessage());
            return "redirect:" + productCategoryPath;
        } catch (AttributeGroupNotFoundException attributeGroupNotFoundException) {
            redirectAttributes.addFlashAttribute("error", attributeGroupNotFoundException.getMessage());
            return "redirect:" + attributeGroupPath;
        }

    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddPage(
            @ModelAttribute("attribute") @Valid Attribute attribute, BindingResult result, ModelMap model,
            RedirectAttributes redirectAttributes) {

        try {
            if (productCategory == null) throw new ProductCategoryNotFoundException();
            if (attributeGroup == null) throw new AttributeGroupNotFoundException();

            if (!result.hasErrors()) {
                redirectAttributes.addFlashAttribute("success", "");
                attributeService.save(attribute, attributeGroup);
                return "redirect:" + relativePath;
            }

            model.put("subPageTitle", "Add");
            model.put("description", "Add attribute for " + attributeGroup.getName());
            model.put("pageTitle", "Add new attribute");
            model.put("action", "add");
            model.put("pathAction", relativePath + "/add");
            model.put("attribute", attribute);

            return "backend/attribute-add";

        } catch (ProductCategoryNotFoundException productCategoryNotFoundException) {
            redirectAttributes.addFlashAttribute("error", productCategoryNotFoundException.getMessage());
            return "redirect:" + productCategoryPath;
        } catch (AttributeGroupNotFoundException attributeGroupNotFoundException) {
            redirectAttributes.addFlashAttribute("error", attributeGroupNotFoundException.getMessage());
            return "redirect:" + attributeGroupPath;
        }


    }

    @RequestMapping(value = "/update/{attributeId:^\\d+}", method = RequestMethod.GET)
    public String updatePage(
            @PathVariable("attributeId") int attributeId, ModelMap model, RedirectAttributes redirectAttributes) {

        try {
            if (productCategory == null) throw new ProductCategoryNotFoundException();
            if (attributeGroup == null) throw new AttributeGroupNotFoundException();

            Attribute attribute = attributeService.getByKey(attributeId);
            if (attribute == null) throw new AttributeNotFoundException();

            model.put("subPageTitle", "Update for " + attribute.getName());
            model.put("description", "Update attribute for " + attributeGroup.getName());
            model.put("pageTitle", "Update attribute");
            model.put("action", "update");
            model.put("pathAction", relativePath + "/update");
            model.put("attribute", attribute);

            return "backend/attribute-add";

        } catch (AttributeNotFoundException attributeGroupNotFound) {
            redirectAttributes.addFlashAttribute("error", attributeGroupNotFound.getMessage());
            return "redirect:" + relativePath;
        } catch (ProductCategoryNotFoundException productCategoryNotFoundException) {
            redirectAttributes.addFlashAttribute("error", productCategoryNotFoundException.getMessage());
            return "redirect:" + productCategoryPath;
        } catch (AttributeGroupNotFoundException attributeGroupNotFoundException) {
            redirectAttributes.addFlashAttribute("error", attributeGroupNotFoundException.getMessage());
            return "redirect:" + attributeGroupPath;
        }

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdatePage(
            @Valid @ModelAttribute("attribute") Attribute attribute,
            BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {

        try {
            if (productCategory == null) throw new ProductCategoryNotFoundException();

            if (!result.hasErrors()) {
                attributeService.update(attribute, attributeGroup);
                redirectAttributes.addFlashAttribute("success", "");
                return "redirect:" + relativePath;
            }

            model.put("subPageTitle", "Update for " + attribute.getName());
            model.put("description", "Update attribute for " + attributeGroup.getName());
            model.put("pageTitle", "Update attribute");
            model.put("action", "update");
            model.put("pathAction", relativePath + "/update");
            model.put("attribute", attribute);

            return "backend/attribute-add";

        } catch (AttributeNotFoundException attributeGroupNotFound) {
            redirectAttributes.addFlashAttribute("error", attributeGroupNotFound.getMessage());
            return "redirect:" + relativePath;
        } catch (ProductCategoryNotFoundException productCategoryNotFoundException) {
            redirectAttributes.addFlashAttribute("error", productCategoryNotFoundException.getMessage());
            return "redirect:" + productCategoryPath;
        } catch (AttributeGroupNotFoundException attributeGroupNotFoundException) {
            redirectAttributes.addFlashAttribute("error", attributeGroupNotFoundException.getMessage());
            return "redirect:" + attributeGroupPath;
        }

    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String processDeleteProvince(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {

        try {
            attributeService.delete(id);
            redirectAttributes.addFlashAttribute("success", true);
        } catch (AttributeNotFoundException | AttributeHasAttributeValuesException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:" + relativePath;
    }
}
