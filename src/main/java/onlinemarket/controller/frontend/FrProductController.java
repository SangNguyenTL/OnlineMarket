package onlinemarket.controller.frontend;

import onlinemarket.controller.MainController;
import onlinemarket.model.Product;
import onlinemarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/product/{slug:[\\w\\d-]+}")
public class FrProductController extends MainController {

    @Autowired
    ProductService productService;


    @ModelAttribute
    public ModelMap populateAttribute(@PathVariable("slug") String slug, ModelMap model) {

        Product product = productService.getByDeclaration("slug", slug);
        title = product.getName();
        model.put("pageTitle", title);
        model.put("product",product);

        List<Product> relatedProducts = productService.getRelatedProduct(product.getProductCategory(),product.getBrand());
        model.put("relatedProducts",relatedProducts);
        return model;
    }

    @RequestMapping( value = "", method = RequestMethod.GET)
    public String mainPage(@ModelAttribute("slug") ModelMap modelMap){

        modelMap.put("pageTitle", "Menu");
        return "frontend/product";
    }
}
