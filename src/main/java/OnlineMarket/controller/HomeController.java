package OnlineMarket.controller;

import javax.servlet.http.HttpServletRequest;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.*;
import OnlineMarket.result.ResultObject;
import OnlineMarket.service.EventService;
import OnlineMarket.util.exception.productCategory.ProductCategoryNotFoundException;
import OnlineMarket.view.FrontendProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.*;

@Controller
@RequestMapping("")
public class HomeController extends MainController {

	@Autowired
	EventService eventService;

	@Autowired
	AuthenticationManager authenticationManager;

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public String homePage(ModelMap model) {


		FilterForm filterForm = new FilterForm();
		Map<ProductCategory,ResultObject<FrontendProduct>> resultObjectList = new HashMap<>();
		filterForm.setOrderBy("releaseDate");
		filterForm.setOrder("desc");
		filterForm.getGroupSearch().put("state", "0");
		for(ProductCategory productCategory : productCategoryList){
			try {
				ResultObject<FrontendProduct> resultObject = productService.frontendProductResultObject(productService.listByProductCategory(productCategory,filterForm));
				if(!resultObject.getList().isEmpty())
					resultObjectList.put(productCategory, resultObject);
			} catch (ProductCategoryNotFoundException e) {

			}
		}

		filterForm.getGroupSearch().remove("state");
		filterForm.getGroupSearch().put("status", "0");
		filterForm.setSize(3);
		filterForm.setOrderBy("createDate");

		model.put("eventList", eventService.list(filterForm).getList());
		model.put("resultObjectList", resultObjectList);
		model.put("pageTitle", "Home");
		return "frontend/index";
	}



	@RequestMapping(value = "/about-us", method = RequestMethod.GET)
	public String contactPage(ModelMap model){
		breadcrumbs.add(new String[]{"/about-us", "About us"});
		model.put("pageTitle", "About us");
		return "frontend/about-us";
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String userProfile(){
		return "redirect:/user/"+currentUser.getId();
	}

	@RequestMapping(value = "/errors", method = RequestMethod.GET)
	public ModelAndView handleError401(HttpServletRequest request) {
		String errorMsg = "";
		int httpErrorCode = (Integer) request
				.getAttribute("javax.servlet.error.status_code");

		switch (httpErrorCode) {
			case 400: {
				errorMsg = "Http Error Code: 400. Bad Request";
				break;
			}
			case 401: {
				errorMsg = "Http Error Code: 401. Unauthorized";
				break;
			}
			case 404: {
				errorMsg = "Http Error Code: 404. Resource not found";
				break;
			}
			case 500: {
				errorMsg = "Http Error Code: 500. Internal Server Error";
				break;
			}
		}
		ModelAndView errorPage = new ModelAndView("backend/error");
		errorPage.addObject("pageTitle", httpErrorCode + " Error");
		errorPage.addObject("errorMsg", errorMsg);
		errorPage.addObject("code", httpErrorCode);
		return errorPage;

	}

}
