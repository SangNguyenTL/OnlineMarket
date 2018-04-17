package onlinemarket.controller.frontend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import onlinemarket.controller.MainController;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.form.filter.ProductCompare;
import onlinemarket.model.ProductCategory;
import onlinemarket.service.ProductCategoryService;
import onlinemarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/compare-product/{productCategorySlug:[\\d\\w-]+}")
public class FrCompareProduct extends MainController {

    @Autowired
    ProductCategoryService productCategoryService;

    @Autowired
    ProductService productService;

    private FilterForm filterForm;

    @RequestMapping( value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String mainPage(@PathVariable("productCategorySlug") String productCategorySlug, @CookieValue("compareList")String listCompare, ModelMap modelMap) throws NoHandlerFoundException {


        ProductCategory productCategory = productCategoryService.getByDeclaration("slug", productCategorySlug);
        if(productCategory == null) throw new NoHandlerFoundException(null,null,null);

        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        List<ProductCompare> productCompareList = new ArrayList<>();
        try {
            productCompareList = objectMapper.readValue(listCompare, typeFactory.constructCollectionType(List.class, ProductCompare.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(productCompareList.isEmpty()) throw new NoHandlerFoundException(null,null,null);
        filterForm = new FilterForm();
        List<Object> listValue = new ArrayList<>();

        for(ProductCompare productCompare : productCompareList){
            if(productCompare.getCateId().equals(productCategory.getId()))listValue.add(productCompare.getId());
        }

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

        modelMap.put("result", productService.listByInList("id", listValue, filterForm));

        return "frontend/compare";
    }


}
