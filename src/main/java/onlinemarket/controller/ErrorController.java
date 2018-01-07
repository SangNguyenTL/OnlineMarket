package onlinemarket.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import onlinemarket.service.config.ConfigurationService;

@ControllerAdvice
public class ErrorController {

	@Autowired
	private ConfigurationService configurationService;
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest, Exception ex) {

        ModelAndView errorPage = new ModelAndView("backend/error");
        
        int httpErrorCode = 500; 
        errorPage.addObject("general", configurationService.getGeneral());
		errorPage.addObject("logo", configurationService.getLogo());
		errorPage.addObject("titlePage", httpErrorCode+" Error");
		errorPage.addObject("errorMsg", "Internal server error");
        errorPage.addObject("code", httpErrorCode);
        ex.printStackTrace();
        return errorPage;
    }
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(value=HttpStatus.METHOD_NOT_ALLOWED)
    public ModelAndView errorRequestMethodNotSupported(HttpServletRequest httpRequest, Exception ex) {

        ModelAndView errorPage = new ModelAndView("backend/error");
        
        int httpErrorCode = 405; 
        errorPage.addObject("general", configurationService.getGeneral());
		errorPage.addObject("logo", configurationService.getLogo());
		errorPage.addObject("titlePage", httpErrorCode+" Error");
		errorPage.addObject("errorMsg", "Request method 'POST' not supported");
        errorPage.addObject("code", httpErrorCode);
        ex.printStackTrace();
        return errorPage;
    }
     
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
    public ModelAndView errorPageNotFound(HttpServletRequest httpRequest) {

        ModelAndView errorPage = new ModelAndView("backend/error");

        int httpErrorCode = 404;
        
        errorPage.addObject("general", configurationService.getGeneral());
		errorPage.addObject("logo", configurationService.getLogo());
		errorPage.addObject("pageTitle", httpErrorCode+" Error");
        errorPage.addObject("errorMsg", "Not found!");
        errorPage.addObject("code", httpErrorCode);
        return errorPage;
    }
}
