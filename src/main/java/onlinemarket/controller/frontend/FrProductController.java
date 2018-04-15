package onlinemarket.controller.frontend;

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Product;
import onlinemarket.model.Rating;
import onlinemarket.model.other.AdvancedValidation;
import onlinemarket.result.ResultObject;
import onlinemarket.service.ProductService;
import onlinemarket.service.RatingService;
import onlinemarket.util.exception.CustomException;
import onlinemarket.util.exception.product.ProductNotFoundException;
import onlinemarket.util.exception.rating.RatingNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.groups.Default;
import java.util.List;

@Controller
@RequestMapping("/product/{slug:[\\w\\d-]+}")
public class FrProductController extends MainController {

    @Autowired
    ProductService productService;

    @Autowired
    RatingService ratingService;

    FilterForm filterForm;

    private Product product;

    @ModelAttribute
    public ModelMap populateAttribute(@PathVariable("slug") String slug, ModelMap model) throws NoHandlerFoundException, ProductNotFoundException {

        product = productService.getByDeclaration("slug", slug);
        relativePath = "/product/"+slug;
        if(product == null)
            throw new NoHandlerFoundException(null, null, null);
            title = product.getName();
        generateBreadcrumbs();
        breadcrumbs.add(new String[]{"/product-category/"+product.getProductCategory().getSlug(), product.getProductCategory().getName()});
        model.put("pageTitle", title);
        model.put("product",product);
        model.put("relativePath",relativePath);

        List<Product> relatedProducts = productService.getRelatedProduct(product.getProductCategory(),product.getBrand());
        model.put("relatedProducts",relatedProducts);

        filterForm = new FilterForm();
        filterForm.setOrderBy("createRateDate");
        filterForm.setOrder("desc");
        filterForm.getGroupSearch().put("state", "Active");
        model.put("filterForm",filterForm);

        return model;
    }

    @RequestMapping( value = "", method = RequestMethod.GET)
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) throws NoHandlerFoundException {
      try{
          modelMap.put("pathAction", relativePath + "/add-rating");
          ResultObject<Rating> resultObject = ratingService.listByProduct(product, filterForm);
          modelMap.put("result", resultObject);
          modelMap.put("pageTitle", "Menu");
          modelMap.put("rating", new Rating());
      } catch (ProductNotFoundException e) {

      }

        return "frontend/product";
    }

    @RequestMapping(value = "/add-rating", method = RequestMethod.POST)
    public String processAddRating(@Validated(value = {Default.class, AdvancedValidation.CheckSlug.class, AdvancedValidation.AddPost.class}) @ModelAttribute("rating") Rating rating, BindingResult result, ModelMap modelMap, RedirectAttributes redirectAttributes) {
        try {
            if (!result.hasErrors()) {
                if(currentUser.getId()==null)
                    throw new CustomException("You must be login.");
                rating.setProduct(product);
                ratingService.save(rating, currentUser);
                redirectAttributes.addFlashAttribute("success", true);
            } else {
                modelMap.put("subPageTitle", "Add");
                modelMap.put("action", "add");
                modelMap.put("pathAction", relativePath + "/add");
                modelMap.put("path", "post-add");
                modelMap.put("rating", rating);

                return "frontend/post-add";
            }

        } catch (RatingNotFoundException|CustomException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:" + relativePath;
    }
}
