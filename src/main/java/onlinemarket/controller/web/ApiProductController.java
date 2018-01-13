package onlinemarket.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import onlinemarket.model.Product;
import onlinemarket.service.ProductService;
import onlinemarket.util.ResponseResult;

@RequestMapping("api/product")
@RestController
public class ApiProductController {

	@Autowired
	ProductService service;

	@RequestMapping(value = "/check-slug", produces = MediaType.APPLICATION_JSON_VALUE, method = { RequestMethod.POST,
			RequestMethod.GET })
	public ResponseEntity<?> checkSlugUnique(@RequestParam(value = "value", required = true) String value,
			@RequestParam(value = "id", required = false) Integer id) {

		Product product = service.getByDeclaration("slug", value);
		boolean flag = true;
		if (product == null)
			flag = false;
		else if(id != null){
			Product oldProduct = service.getByKey(id);
			if (product != null && oldProduct.equals(product))
				flag = false;
		}
		return ResponseEntity.ok(new ResponseResult(flag, null));
	}
}
