package onlinemarket.controller.frontend;

import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Product;
import onlinemarket.model.Rating;
import onlinemarket.result.ResultObject;
import onlinemarket.service.ProductService;
import onlinemarket.service.RatingService;
import onlinemarket.util.exception.product.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

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

        List<Product> relatedProducts = productService.getRelatedProduct(product.getProductCategory(),product.getBrand());
        model.put("relatedProducts",relatedProducts);

        filterForm = new FilterForm();
        filterForm.setOrderBy("createRateDate");
        filterForm.setOrder("desc");
        filterForm.getGroupSearch().put("state", "Active");


        return model;
    }

    @RequestMapping( value = "", method = RequestMethod.GET)
    public String mainPage(@ModelAttribute("filterForm") FilterForm filterForm, ModelMap modelMap) throws NoHandlerFoundException {
      try{
          ResultObject<Rating> resultObject = ratingService.listByProduct(product,filterForm);
          if(!resultObject.getList().isEmpty())
              modelMap.put("result", resultObject);
          modelMap.put("pageTitle", "Menu");
      } catch (ProductNotFoundException e) {

      }

        return "frontend/product";
    }
}
