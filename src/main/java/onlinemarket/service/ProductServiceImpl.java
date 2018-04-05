package onlinemarket.service;

import java.util.List;

import onlinemarket.util.exception.CustomException;
import onlinemarket.util.exception.productCategory.ProductCategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.ProductDao;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.model.Product;
import onlinemarket.model.ProductCategory;
import onlinemarket.result.ResultObject;

@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductDao productDao;

	@Override
	public Product getByKey(Integer key) {
		return productDao.getByKey(key);
	}

	@Override
	public Product getByDeclaration(String key, Object value) {
		return productDao.getByDeclaration(key, value);
	}

	@Override
	public ResultObject<Product> listByProductCategory(ProductCategory productCategory, FilterForm filterForm) throws ProductCategoryNotFoundException {
		if(productCategory == null) throw new ProductCategoryNotFoundException();
		return productDao.listByDeclaration("productCategory", productCategory, filterForm);
	}

}
