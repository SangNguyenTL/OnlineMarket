package onlinemarket.controller.web;

import onlinemarket.form.filter.SearchSelect;
import onlinemarket.result.api.ResultProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import onlinemarket.model.Product;
import onlinemarket.service.ProductService;
import onlinemarket.util.ResponseResult;

@RequestMapping("api/product")
@RestController
public class ApiProductController {

	@Autowired
	ProductService productService;

	@RequestMapping(value = "/check-slug", produces = MediaType.APPLICATION_JSON_VALUE, method = { RequestMethod.POST,
			RequestMethod.GET })
	public ResponseEntity<?> checkSlugUnique(@RequestParam(value = "value", required = true) String value,
			@RequestParam(value = "id", required = false) Integer id) {

		Product product = productService.getByDeclaration("slug", value);
		boolean flag = true;
		if (product == null)
			flag = false;
		else if(id != null){
			Product oldProduct = productService.getByKey(id);
			if (product != null && oldProduct.equals(product))
				flag = false;
		}
		return ResponseEntity.ok(new ResponseResult(flag, null));
	}

	@RequestMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<?> search(@ModelAttribute SearchSelect searchSelect){
		if(searchSelect.getQ().length() > 3)
			return ResponseEntity.ok(productService.searchSelect(searchSelect));
		else  return ResponseEntity.ok(new ResultProduct());
	}
}
