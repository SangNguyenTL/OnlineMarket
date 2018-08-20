package OnlineMarket.controller.user;

import OnlineMarket.controller.MainController;
import OnlineMarket.model.User;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

public abstract class UserControllerInterface extends MainController {

    protected User user;

    String userProfilePath;

    @ModelAttribute
    public void constructModel(ModelMap modelMap, @PathVariable("userId") Integer userId, HttpServletRequest request) throws NoHandlerFoundException {

        boolean enable = true;
        user = userService.getByKey(userId);
        if(user == null)enable = false;
        currentUser = userService.getCurrentUser();
        switch (currentUser.getRole().getName()){
            case "SUPER_ADMIN":
                break;
            case "ADMIN":
                switch (user.getRole().getName()){
                    case "SUPER_ADMIN":
                        enable = false;
                        break;
                    case "ADMIN":
                        if(!user.getId().equals(currentUser.getId())) enable = false;
                        break;
                }
                break;
            case  "USER":
                switch (user.getRole().getName()){
                    case "SUPER_ADMIN":
                    case "ADMIN":
                        enable = false;
                        break;
                    case "USER":
                        if(!user.getId().equals(currentUser.getId())) enable = false;
                        break;
                }
                break;
            default:
                enable = false;
        }
        if(!enable) throw new NoHandlerFoundException(request.getMethod(), request.getRequestURL().toString(), null);


        userProfilePath = "/user/"+userId;
        relativePath = userProfilePath;
        generateBreadcrumbs();
        breadcrumbs.add(new String[]{userProfilePath, "User"});
        modelMap.put("breadcrumbs", breadcrumbs);
        modelMap.put("user", user);
        modelMap(modelMap);
    }

    public abstract ModelMap modelMap(ModelMap modelMap);
}
