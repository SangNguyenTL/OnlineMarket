package OnlineMarket.controller.frontend;

import OnlineMarket.controller.MainController;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Product;
import OnlineMarket.model.Rating;
import OnlineMarket.util.exception.user.UserNotFoundException;
import OnlineMarket.util.group.AdvancedValidation;
import OnlineMarket.result.ResultObject;
import OnlineMarket.service.ProductService;
import OnlineMarket.service.ProductViewsService;
import OnlineMarket.service.RatingService;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.exception.product.ProductNotFoundException;
import OnlineMarket.util.other.State;
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

@Controller
@RequestMapping("/product/{slug:[\\w\\d-]+}")
public class FrProductController extends MainController {

    @Autowired
    ProductService productService;

    @Autowired
    RatingService ratingService;

    @Autowired
    ProductViewsService productViewsService;

    private FilterForm filterForm;

    private Product product;

    private String[] strings;

    @Override
    protected void addMeta(ModelMap modelMap) {
        super.addMeta(modelMap);
        breadcrumbs.add(strings);
    }

    @ModelAttribute
    public ModelMap modelAttribute(@PathVariable("slug") String slug, ModelMap model) throws NoHandlerFoundException{

        product = productService.getByDeclaration("slug", slug);
        relativePath = "/product/" + slug;
        if (product == null || product.getState() == 3)
            throw new NoHandlerFoundException(null, null, null);
        productViewsService.save(product);
        strings = new String[]{"/product-category/" + product.getProductCategory().getSlug(), product.getProductCategory().getName()};
        title = product.getName();
        model.put("pageTitle", title);
        model.put("product", productService.convertProductToFrProduct(product));
        model.put("relativePath", relativePath);

        model.put("relatedProducts", productService.convertProductToFrProduct(productService.getRelatedProduct(product.getProductCategory(), product.getBrand(), product)));

        filterForm = new FilterForm();
        filterForm.setOrderBy("createRateDate");
        filterForm.setOrder("desc");
        filterForm.getGroupSearch().put("state", State.ACTIVE.name());
        model.put("filterForm", filterForm);

        return model;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap){
        try {
            modelMap.put("pathAction", relativePath + "/add-rating");
            ResultObject<Rating> resultObject = ratingService.listByProduct(product, filterForm);
            modelMap.put("result", resultObject);
            modelMap.put("rating", new Rating());
        } catch (ProductNotFoundException ignore) {

        }

        return "frontend/product";
    }

    @RequestMapping(value = "/page/{page:\\d+}", method = RequestMethod.GET)
    public String mainPagePagination(@PathVariable("page") Integer page, @ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) {
        try {
            filterForm.setCurrentPage(page);

            modelMap.put("pathAction", relativePath + "/add-rating");
            ResultObject<Rating> resultObject = ratingService.listByProduct(product, filterForm);
            modelMap.put("result", resultObject);
            modelMap.put("pageTitle", "Pages "+page+" | "+product.getName());
            modelMap.put("rating", new Rating());
        } catch (ProductNotFoundException ignore) {

        }
        return "frontend/product";
    }

    @RequestMapping(value = "/add-rating", method = RequestMethod.POST)
    public String processAddRating(@Validated(value = {Default.class, AdvancedValidation.AddNew.class}) @ModelAttribute("rating") Rating rating, BindingResult result, ModelMap modelMap, RedirectAttributes redirectAttributes) {
        try {
            if (currentUser.getId() == null) throw new CustomException("You must be login.");
            if (ratingService.getByUserIdAndProductId(rating.getUser().getId(), rating.getProduct().getId()) != null) throw new CustomException("You have rated, can not evaluate further");
            if (!result.hasErrors()) {


                ratingService.save(rating);
                redirectAttributes.addFlashAttribute("success", true);
                redirectAttributes.addFlashAttribute("ratingPage", true);
            }else {
                modelMap.put("ratingPage", true);
                modelMap.put("pathAction", relativePath + "/add-rating");
                ResultObject<Rating> resultObject = ratingService.listByProduct(product, filterForm);
                modelMap.put("result", resultObject);
                modelMap.put("rating", rating);

                return "frontend/product";
            }

        } catch (UserNotFoundException|CustomException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("ratingPage", true);
        } catch (ProductNotFoundException ignore) {

        }

        return "redirect:" + relativePath;
    }
}
