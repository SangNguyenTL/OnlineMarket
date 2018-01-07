package onlinemarket.controller.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("")
public class MainRestController {
	
    @RequestMapping(value="/error", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ModelAndView handleError401(HttpServletRequest request)   {
        ModelAndView errorPage = new ModelAndView("backend/error");
        int httpErrorCode = 401;
        errorPage.addObject("pageTitle", httpErrorCode+ " Error");
        errorPage.addObject("errorMsg", "You do not have permission to access here!");
        errorPage.addObject("code", httpErrorCode);
        return errorPage;
        
    }
    
}
