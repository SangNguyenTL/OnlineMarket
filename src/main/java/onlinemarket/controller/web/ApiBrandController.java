package onlinemarket.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import onlinemarket.model.Brand;
import onlinemarket.service.BrandService;
import onlinemarket.util.ResponseResult;

@RequestMapping("api/brand")
@RestController
public class ApiBrandController {

	@Autowired
	BrandService brandService;
	
	@RequestMapping(value = "/check-slug", produces = MediaType.APPLICATION_JSON_VALUE, params = {"value"})
	public ResponseEntity<?> checkSlugUniqeu(@RequestParam("value") String value){
		
		Brand brand = brandService.getByDeclaration("slug", value);
		return ResponseEntity.ok(new ResponseResult(brand == null, null));
		
	}
}
