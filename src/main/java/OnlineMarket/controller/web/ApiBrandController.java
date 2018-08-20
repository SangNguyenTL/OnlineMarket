package OnlineMarket.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import OnlineMarket.model.Brand;
import OnlineMarket.service.BrandService;
import OnlineMarket.util.ResponseResult;

@RequestMapping("api/brand")
@RestController
public class ApiBrandController {

	@Autowired
	BrandService brandService;

	@RequestMapping(value = "/check-slug", produces = MediaType.APPLICATION_JSON_VALUE, method = { RequestMethod.POST,
			RequestMethod.GET })
	public ResponseEntity<?> checkSlugUnique(@RequestParam(value = "value", required = true) String value,
			@RequestParam(value = "id", required = false) Integer id) {

		Brand brand = brandService.getByDeclaration("slug", value);
		boolean flag = true;
		if (brand == null)
			flag = false;
		else if(id != null){
			Brand oldBrand = brandService.getByKey(id);
			if (oldBrand != null && oldBrand.equals(brand))
				flag = false;
		}
		return ResponseEntity.ok(new ResponseResult(flag, null));
	}
}
