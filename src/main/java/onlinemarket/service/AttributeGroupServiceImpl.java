package onlinemarket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.AttributeGroupDao;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.AttributeGroup;
import onlinemarket.model.ProductCategory;
import onlinemarket.result.ResultObject;

@Service("attributeGroupServiceImpl")
@Transactional
public class AttributeGroupServiceImpl implements AttributeGroupService{

	@Autowired
	AttributeGroupDao attributeGroupDao;
	
	@Override
	public void save(AttributeGroup entity) {
		attributeGroupDao.save(entity);
	}

	@Override
	public void update(AttributeGroup entity) {
		attributeGroupDao.update(entity);
	}

	@Override
	public void delete(AttributeGroup entity) {
		attributeGroupDao.delete(entity);
	}

	@Override
	public AttributeGroup getByKey(Integer key) {
		return attributeGroupDao.getByKey(key);
	}

	@Override
	public AttributeGroup getByDeclaration(String key, String value) {
		return attributeGroupDao.getByDeclaration(key, value);
	}

	@Override
	public List<AttributeGroup> list() {
		return attributeGroupDao.list();
	}

	@Override
	public List<AttributeGroup> list(Integer offset, Integer maxResults) {
		return attributeGroupDao.list(offset, maxResults);
	}
	
	@Override
	public AttributeGroup getByProductCategory(ProductCategory productCategory) {
		return attributeGroupDao.getByProductCategory(productCategory);
	}

	@Override
	public ResultObject<AttributeGroup> listByProductCategory(ProductCategory productCategory, FilterForm filterForm) {
		return attributeGroupDao.listByProductCategory(productCategory, filterForm);
	}
}
