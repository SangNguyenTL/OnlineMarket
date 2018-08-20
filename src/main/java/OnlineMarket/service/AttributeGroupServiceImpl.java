package OnlineMarket.service;

import java.util.List;

import OnlineMarket.dao.AttributeDao;
import OnlineMarket.util.exception.attributeGroup.AttributeGroupHasAttributeException;
import OnlineMarket.util.exception.attributeGroup.AttributeGroupNotFoundException;
import OnlineMarket.util.exception.productCategory.ProductCategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import OnlineMarket.dao.AttributeGroupDao;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.AttributeGroup;
import OnlineMarket.model.ProductCategory;
import OnlineMarket.result.ResultObject;

@Service("attributeGroupServiceImpl")
@Transactional
public class AttributeGroupServiceImpl implements AttributeGroupService{

	@Autowired
	AttributeGroupDao attributeGroupDao;

	@Autowired
	AttributeDao attributeDao;

	@Override
	public void save(AttributeGroup entity, ProductCategory productCategory) throws ProductCategoryNotFoundException {
		if(productCategory == null) throw new ProductCategoryNotFoundException();
		entity.setProductCategory(productCategory);
		attributeGroupDao.save(entity);
	}

	@Override
	public void update(AttributeGroup entity, ProductCategory productCategory) throws ProductCategoryNotFoundException, AttributeGroupNotFoundException {
		if(productCategory == null) throw new ProductCategoryNotFoundException();
		AttributeGroup attributeGroup = attributeGroupDao.getByKey(entity.getId());
		if(attributeGroup == null) throw new AttributeGroupNotFoundException();
		entity.setProductCategory(productCategory);
		attributeGroupDao.update(entity);
	}

	@Override
	public void delete(Integer id) throws AttributeGroupNotFoundException, AttributeGroupHasAttributeException {
		AttributeGroup attributeGroup = attributeGroupDao.getByKey(id);
		if(attributeGroup == null) throw new AttributeGroupNotFoundException();
		if(attributeDao.getUniqueResultBy("attributeGroup",attributeGroup) != null) throw new AttributeGroupHasAttributeException();
		attributeGroupDao.delete(attributeGroup);
	}

	@Override
	public AttributeGroup getByKey(Integer key) {
		return attributeGroupDao.getByKey(key);
	}

	@Override
	public AttributeGroup getByDeclaration(String key, Object value) {
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
	public AttributeGroup getByProductCategory(ProductCategory productCategory)  throws ProductCategoryNotFoundException {
		if(productCategory == null) throw new ProductCategoryNotFoundException();
		return attributeGroupDao.getByProductCategory(productCategory);
	}

	@Override
	public ResultObject<AttributeGroup> listByProductCategory(ProductCategory productCategory, FilterForm filterForm) throws ProductCategoryNotFoundException {
		if(productCategory == null) throw new ProductCategoryNotFoundException();
		return attributeGroupDao.listByProductCategory(productCategory, filterForm);
	}

	@Override
	public List<AttributeGroup> listByProductCategory(ProductCategory productCategory) throws ProductCategoryNotFoundException {
		return attributeGroupDao.listByProductCategory(productCategory);
	}
}
