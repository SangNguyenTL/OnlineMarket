package OnlineMarket.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import OnlineMarket.model.ProductCategory;
import OnlineMarket.service.ProductCategoryService;
import OnlineMarket.util.ResponseResult;

@RequestMapping("api/product-category")
@RestController
public class ApiProductCategoryController {

	@Autowired
	ProductCategoryService productCategoryService;

	@RequestMapping(value = "/check-slug", produces = MediaType.APPLICATION_JSON_VALUE, method = { RequestMethod.POST,
			RequestMethod.GET })
	public ResponseEntity<?> checkSlugUnique(@RequestParam(value = "value", required = true) String value,
			@RequestParam(value = "id", required = false) Integer id) {

		ProductCategory productCategory = productCategoryService.getByDeclaration("slug", value);
		boolean flag = true;
		if (productCategory == null)
			flag = false;
		else if (id != null) {
			ProductCategory oldProductCategory = productCategoryService.getByKey(id);
			if (oldProductCategory != null && oldProductCategory.equals(productCategory))
				flag = false;
		}
		return ResponseEntity.ok(new ResponseResult(flag, null));
	}
}
