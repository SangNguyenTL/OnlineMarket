package OnlineMarket.controller.web;

import OnlineMarket.form.filter.SearchSelect;
import OnlineMarket.result.api.ResultProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import OnlineMarket.model.Product;
import OnlineMarket.service.ProductService;
import OnlineMarket.result.api.ResponseResult;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/product")
@RestController
public class ApiProductController {

	@Autowired
	ProductService productService;

	@RequestMapping(value = "/check-slug", produces = MediaType.APPLICATION_JSON_VALUE, method = { RequestMethod.POST,
			RequestMethod.GET })
	public ResponseEntity<?> checkSlugUnique(@RequestParam(value = "value") String value,
			@RequestParam(value = "id", required = false) Integer id) {

		Product product = productService.getByDeclaration("slug", value);
		boolean flag = true;
		if (product == null)
			flag = false;
		else if(id != null){
			Product oldProduct = productService.getByKey(id);
			if (oldProduct != null && oldProduct.equals(product))
				flag = false;
		}
		return ResponseEntity.ok(new ResponseResult(flag, ""));
	}

	@RequestMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<?> search(@ModelAttribute SearchSelect searchSelect){
		if(searchSelect.getQ().length() > 3)
			return ResponseEntity.ok(productService.searchSelect(searchSelect));
		else  return ResponseEntity.ok(new ResultProduct());
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@RequestParam("id") Integer id){
		boolean error = false;
		Product product = productService.getByKey(id);
		List<Product> list = new ArrayList<>();
 		if(product!=null) list.add(product);
		return ResponseEntity.ok(new ResponseResult(error, list));
	}
}
