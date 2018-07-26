package onlinemarket.controller.frontend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.ProductCategory;
import onlinemarket.service.ProductCategoryService;
import onlinemarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/compare-product/{productCategorySlug:[\\d\\w-]+}")
public class FrCompareProduct extends MainController {

    @Autowired
    ProductCategoryService productCategoryService;

    @Autowired
    ProductService productService;

    private FilterForm filterForm;

    @RequestMapping( value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@PathVariable("productCategorySlug") String productCategorySlug, @CookieValue(value = "compareList", required = false)String listCompare, ModelMap modelMap) throws NoHandlerFoundException {


        ProductCategory productCategory = productCategoryService.getByDeclaration("slug", productCategorySlug);
        if(productCategory == null) throw new NoHandlerFoundException(null,null,null);

        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        HashMap<String,List<Object>> productCompareList = new HashMap<>();
        try {
            if(listCompare != null)
                productCompareList = objectMapper.readValue(listCompare, HashMap.class);
        } catch (IOException e) {

        }

        filterForm = new FilterForm();
        List<Object> listValue = productCompareList.get(productCategory.getId().toString());

        filterForm.getPrivateGroupSearch().put("state", "0");
        filterForm.setOrderBy("createDate");
        filterForm.setOrder("desc");
        filterForm.setSize(3);
        title = "Compare "+productCategory.getName();
        generateBreadcrumbs();
        relativePath = "/compare-product/"+productCategorySlug;
        modelMap.put("title", title);
        breadcrumbs.add(new String[]{"/product-category/"+productCategorySlug, productCategory.getName()});
        breadcrumbs.add(new String[]{relativePath, "Compare"});
        modelMap.put("relativePath", relativePath);

        modelMap.put("productCategory", productCategory);
        if(listValue instanceof List && !listValue.isEmpty())
            modelMap.put("result", productService.frontendProductResultObject(productService.listByInList("id", listValue, filterForm)));

        return "frontend/compare";
    }


}
