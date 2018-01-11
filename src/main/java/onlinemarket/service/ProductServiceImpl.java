package onlinemarket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.ProductDao;
import onlinemarket.model.Brand;
import onlinemarket.model.Event;
import onlinemarket.model.Product;
import onlinemarket.model.ProductCategory;

@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService{

	@Autowired
	ProductDao productDao;
	
	@Override
	public void save(Product entity) {
		productDao.save(entity);
	}

	@Override
	public void update(Product entity) {
		productDao.update(entity);
	}

	@Override
	public void delete(Product entity) {
		productDao.delete(entity);
	}

	@Override
	public Product getByKey(Integer key) {
		return productDao.getByKey(key);
	}

	@Override
	public Product getByDeclaration(String key, String value) {
		return productDao.getByDeclaration(key, value);
	}

	@Override
	public List<Product> list() {
		return productDao.list();
	}

	@Override
	public List<Product> list(Integer offset, Integer maxResults) {
		return productDao.list(offset, maxResults);
	}

	@Override
	public Product getByBrand(Brand brand) {
		return productDao.getByBrand(brand);
	}

	@Override
	public Product getByProductCategory(ProductCategory productCategory) {
		return productDao.getByProductCategory(productCategory);
	}

	@Override
	public Product getByEvent(Event event) {
		return productDao.getByEvent(event);
	}

}
