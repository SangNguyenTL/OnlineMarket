package OnlineMarket.controller;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.ProductCategory;
import OnlineMarket.service.*;
import OnlineMarket.util.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import OnlineMarket.model.User;
import OnlineMarket.service.config.ConfigurationService;
import OnlineMarket.util.Slugify;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public abstract class MainController {

    @Autowired
    protected ProductService productService;

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Autowired
    protected ConfigurationService configurationService;

    @Autowired
    protected ProductCategoryService productCategoryService;

    @Autowired
    protected BrandService brandService;

    @Autowired
    protected UserService userService;

    @Autowired
    MenuSiteService menuSiteService;

    @Autowired
    protected NotificationService notificationService;

    protected User currentUser;

    @Autowired
    protected Slugify slg;

    protected String relativePath;

    protected String title;

    protected List<String[]> breadcrumbs;

    protected List<ProductCategory> productCategoryList;

    private boolean pathMatches(String [] patterns, String path){
        for(String pattern : patterns){
            if(Pattern.matches(pattern, path)) return true;
        }
        return false;
    }

    @ModelAttribute
    public void populateMetaPage(ModelMap model, HttpServletRequest request) {
        String restOfTheUrl = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        productCategoryList = productCategoryService.list();

        currentUser = userService.getCurrentUser();

        if(pathMatches(new String[]{ "(?!^/admin/?)(^.+)*"}, restOfTheUrl)){
            model.put("menuSite", menuSiteService.generateMenu());
        }

        if(pathMatches(new String[]{"/admin/config/menu-position", "(?!^/admin/?)(^.+)*"}, restOfTheUrl)){
            model.put("menuPosition", configurationService.getMenuPositionConfig());
        }

        if(pathMatches(new String[]{"/admin/config/contact", "(?!^/admin/?)(^.+)*"}, restOfTheUrl)){
            model.put("contact", configurationService.getContact());
        }

        if(pathMatches(new String[]{"/admin/product-category/\\d+/product/(add|update/\\d+)", "(?!^/admin/?)(^.+)*"}, restOfTheUrl)){
            model.put("brandList", brandService.list());
        }

        if(pathMatches(new String[]{"/admin/config/upload"}, restOfTheUrl)){
            model.put("upload", configurationService.getUpload());
        }

        if(pathMatches(new String[]{"/(post|page|product|(post|product)-category|brand)(/[\\w\\d-]*)*", "/login", "/register", "/forgot-password", "/change-password", "/event", "/?" }, restOfTheUrl)){
            populateAttribute(model);
            generateBreadcrumbs();
        }

        if(currentUser != null){
            try {
                model.put("countNotify", notificationService.countByUser());
            } catch (CustomException ignore) {
            }
        }

        addMeta(model);

        model.put("general", configurationService.getGeneral());
        model.put("logo", configurationService.getLogo());
        model.put("api", configurationService.getApiConfig());
        model.put("social", configurationService.getSocial());
        model.put("productCategoryList", productCategoryList);

        model.put("breadcrumbs", breadcrumbs);
        model.put("currentUser", currentUser);
    }

    protected void addMeta(ModelMap modelMap){

    }

    private void populateAttribute(ModelMap model) {

        FilterForm filterFormTopList = new FilterForm();
        filterFormTopList.getGroupSearch().put("state", "0");
        filterFormTopList.setSize(5);
        filterFormTopList.setOrder("desc");

        filterFormTopList.setOrderBy("numberOrder");
        model.put("productBestSellerList", productService.convertProductToFrProduct(productService.list(filterFormTopList).getList()));

        filterFormTopList.setOrderBy("productViewsStatistic.total");
        model.put("productBestViewing", productService.convertProductToFrProduct(productService.list(filterFormTopList).getList()));

        filterFormTopList.setOrderBy("ratingStatistic.totalScore");
        model.put("productBestRating",productService.convertProductToFrProduct(productService.list(filterFormTopList).getList()));

    }

    @PostConstruct
    public void init() {
        requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);
    }

    protected void generateBreadcrumbs(){
        if(breadcrumbs == null) breadcrumbs = new ArrayList<>();
        else breadcrumbs.clear();
        breadcrumbs.add(new String[] {"/", "Home"});
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
