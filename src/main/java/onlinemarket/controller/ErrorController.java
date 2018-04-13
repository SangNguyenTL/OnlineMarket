package onlinemarket.controller;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.QueryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import onlinemarket.service.config.ConfigurationService;

@ControllerAdvice(annotations = Controller.class)
@Order(2)
public class ErrorController {

	@Autowired
	private ConfigurationService configurationService;

	@ExceptionHandler()
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ModelAndView renderErrorPage(HttpServletRequest httpRequest, Exception ex) {

		ModelAndView errorPage = new ModelAndView("backend/error");

		int httpErrorCode = 500;
		errorPage.addObject("general", configurationService.getGeneral());
		errorPage.addObject("logo", configurationService.getLogo());
		errorPage.addObject("titlePage", httpErrorCode + " Error");
		errorPage.addObject("errorMsg", ex.getLocalizedMessage());
		errorPage.addObject("code", httpErrorCode);
//		errorPage.addObject("exception", ex);

		return errorPage;
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
	public ModelAndView errorRequestMethodNotSupported(HttpServletRequest httpRequest, Exception ex) {

		ModelAndView errorPage = new ModelAndView("backend/error");

		int httpErrorCode = 405;
		errorPage.addObject("general", configurationService.getGeneral());
		errorPage.addObject("logo", configurationService.getLogo());
		errorPage.addObject("titlePage", httpErrorCode + " Error");
		errorPage.addObject("errorMsg", ex.getMessage());
		errorPage.addObject("code", httpErrorCode);
		ex.printStackTrace();
		return errorPage;
	}

	@ExceptionHandler(value = { MissingServletRequestParameterException.class, QueryException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView missingServletRequestParameterException(HttpServletRequest httpRequest, Exception ex) {

		ModelAndView errorPage = new ModelAndView("backend/error");

		int httpErrorCode = 400;
		errorPage.addObject("general", configurationService.getGeneral());
		errorPage.addObject("logo", configurationService.getLogo());
		errorPage.addObject("titlePage", httpErrorCode + " Error");
		errorPage.addObject("errorMsg", ex.getMessage());
		errorPage.addObject("code", httpErrorCode);
		ex.printStackTrace();
		return errorPage;
	}

	@ExceptionHandler({NoHandlerFoundException.class})
	public ModelAndView errorPageNotFound(NoHandlerFoundException ex) {

		ModelAndView errorPage = new ModelAndView("backend/error");

		int httpErrorCode = 404;

		errorPage.addObject("general", configurationService.getGeneral());
		errorPage.addObject("logo", configurationService.getLogo());
		errorPage.addObject("pageTitle", httpErrorCode + " Error");
		errorPage.addObject("errorMsg", "Not found!");
		errorPage.addObject("code", httpErrorCode);

		return errorPage;
	}

	@ExceptionHandler({InvalidCsrfTokenException.class, CookieTheftException.class})
	@ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT)
	public ModelAndView errorPageTimeOut(HttpServletRequest httpRequest, Exception ex){
		ModelAndView errorPage = new ModelAndView("backend/error");

		int httpErrorCode = HttpStatus.REQUEST_TIMEOUT.value();

		errorPage.addObject("general", configurationService.getGeneral());
		errorPage.addObject("logo", configurationService.getLogo());
		errorPage.addObject("pageTitle", httpErrorCode + " Error");
		errorPage.addObject("errorMsg", ex.getLocalizedMessage());
		errorPage.addObject("code", httpErrorCode);

		return errorPage;
	}

}
