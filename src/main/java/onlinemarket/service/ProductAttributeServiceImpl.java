package onlinemarket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.ProductAttributeDao;
import onlinemarket.model.Attribute;
import onlinemarket.model.ProductAttribute;
import onlinemarket.model.ProductAttributeId;

@Service("productAttributeService")
@Transactional
public class ProductAttributeServiceImpl implements ProductAttributeService{
	
	@Autowired
	ProductAttributeDao productAttributeDao;

	@Override
	public void save(ProductAttribute entity) {
		productAttributeDao.save(entity);
	}

	@Override
	public void update(ProductAttribute entity) {
		productAttributeDao.update(entity);
	}

	@Override
	public void delete(ProductAttribute entity) {
		productAttributeDao.delete(entity);
	}

	@Override
	public ProductAttribute getByKey(ProductAttributeId key) {
		return productAttributeDao.getByKey(key);
	}

	@Override
	public ProductAttribute getByDeclaration(String key, String value) {
		return productAttributeDao.getByDeclaration(key, value);
	}

	@Override
	public List<ProductAttribute> list() {
		return productAttributeDao.list();
	}

	@Override
	public List<ProductAttribute> list(Integer offset, Integer maxResults) {
		return productAttributeDao.list(offset, maxResults);
	}

	@Override
	public ProductAttribute getByAttribute(Attribute attribute) {
		return productAttributeDao.getByAttribute(attribute);
	}
}
