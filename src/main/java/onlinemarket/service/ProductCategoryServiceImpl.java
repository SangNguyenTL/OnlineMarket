package onlinemarket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.ProductCategoryDao;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.ProductCategory;
import onlinemarket.result.ResultObject;

@Service("productCategoryService")
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService{

	@Autowired
	ProductCategoryDao productCategoryDao;

	@Override
	public void save(ProductCategory entity) {
		productCategoryDao.save(entity);
	}

	@Override
	public void update(ProductCategory entity) {
		productCategoryDao.update(entity);
	}

	@Override
	public void delete(ProductCategory entity) {
		productCategoryDao.delete(entity);
	}

	@Override
	public ProductCategory getByKey(Integer key) {
		return productCategoryDao.getByKey(key);
	}

	@Override
	public ProductCategory getByDeclaration(String key, String value) {
		return productCategoryDao.getByDeclaration(key, value);
	}

	@Override
	public List<ProductCategory> list() {
		return productCategoryDao.list();
	}

	@Override
	public List<ProductCategory> list(Integer offset, Integer maxResults) {
		return productCategoryDao.list(offset, maxResults);
	}

	@Override
	public ResultObject<ProductCategory> list(FilterForm filterForm) {
		return productCategoryDao.list(filterForm);
	}

}
