package onlinemarket.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import onlinemarket.model.User;
import onlinemarket.service.BrandService;
import onlinemarket.service.ProductCategoryService;
import onlinemarket.service.UserService;
import onlinemarket.service.config.ConfigurationService;
import onlinemarket.util.Slugify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class MainController {

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

    protected User currentUser;

    @Autowired
    protected Slugify slg;

    protected String relativePath;

    protected String title;

    protected List<String[]> breadcrumbs;

    @ModelAttribute
    public void populateMetaPage(ModelMap model) {
        model.put("general", configurationService.getGeneral());
        model.put("api", configurationService.getApiConfig());
        model.put("logo", configurationService.getLogo());
        model.put("contact", configurationService.getContag());
        model.put("productCategoryList", productCategoryService.list());
        model.put("brandList", brandService.list());
    }

    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }

        currentUser = userService.getByEmail(userName);
        if (currentUser == null) {
            currentUser = new User();
            currentUser.setEmail(userName);
        }
        return currentUser;
    }

    @ModelAttribute("breadcrumbs")
    public List<String[]> getBreadcrumbs(){
        return breadcrumbs;
    }

    @PostConstruct
    public void init() {
        requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);
    }

    protected List<String[]> generateBreadcrumbs(){
        if(breadcrumbs == null) breadcrumbs = new ArrayList<>();
        else breadcrumbs.clear();
        breadcrumbs.add(new String[] {"/", "Home"});
        return breadcrumbs;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
