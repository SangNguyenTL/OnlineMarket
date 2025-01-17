package OnlineMarket.controller.frontend;

import com.fasterxml.jackson.databind.ObjectMapper;
import OnlineMarket.controller.MainController;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.ProductCategory;
import OnlineMarket.service.ProductCategoryService;
import OnlineMarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/compare-product/{productCategorySlug:[\\d\\w-]+}")
public class FrCompareProduct extends MainController {

    @Autowired
    ProductCategoryService productCategoryService;

    @Autowired
    ProductService productService;

    @RequestMapping( value = "", method = {RequestMethod.GET, RequestMethod.POST})
    @SuppressWarnings("unchecked")
    public String mainPage(@PathVariable("productCategorySlug") String productCategorySlug, @CookieValue(value = "compareList", required = false) String listCompare, ModelMap modelMap) throws NoHandlerFoundException {

        ProductCategory productCategory = productCategoryService.getByDeclaration("slug", productCategorySlug);
        if(productCategory == null) throw new NoHandlerFoundException(null,null,null);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,List<Object>> productCompareList = new HashMap<>();
        try {
            if(listCompare != null)
                productCompareList = objectMapper.readValue(listCompare, HashMap.class);
        } catch (IOException ignore) {

        }

        FilterForm filterForm = new FilterForm();
        List<Object> listValue = productCompareList.get(productCategory.getId().toString());

        filterForm.getGroupSearch().put("state", "0");
        filterForm.setOrderBy("createDate");
        filterForm.setOrder("desc");
        filterForm.setSize(3);
        title = "Compare "+productCategory.getName();

        relativePath = "/compare-product/"+productCategorySlug;
        modelMap.put("title", title);
        generateBreadcrumbs();
        breadcrumbs.add(new String[]{"/product-category/"+productCategorySlug, productCategory.getName()});
        breadcrumbs.add(new String[]{relativePath, "Compare"});
        modelMap.put("relativePath", relativePath);
        modelMap.put("pageTitle", productCategory.getName()+" Compare");

        modelMap.put("productCategory", productCategory);
        if(listValue != null && !listValue.isEmpty())
            modelMap.put("result", productService.frontendProductResultObject(productService.listByInList("id", listValue, filterForm)));

        return "frontend/compare";
    }


}
