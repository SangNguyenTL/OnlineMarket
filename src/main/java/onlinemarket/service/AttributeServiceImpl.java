package onlinemarket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.AttributeDao;
import onlinemarket.model.Attribute;
import onlinemarket.model.AttributeGroup;

@Service("attributeService")
@Transactional
public class AttributeServiceImpl implements AttributeService{

	@Autowired
	AttributeDao attributeDao;
	
	@Override
	public void save(Attribute entity) {
		attributeDao.save(entity);
	}

	@Override
	public void update(Attribute entity) {
		attributeDao.update(entity);
	}

	@Override
	public void delete(Attribute entity) {
		attributeDao.delete(entity);
	}

	@Override
	public Attribute getByKey(Integer key) {
		return attributeDao.getByKey(key);
	}

	@Override
	public Attribute getByDeclaration(String key, String value) {
		return attributeDao.getByDeclaration(key, value);
	}

	@Override
	public List<Attribute> list() {
		return attributeDao.list();
	}

	@Override
	public List<Attribute> list(Integer offset, Integer maxResults) {
		return attributeDao.list(offset, maxResults);
	}

	@Override
	public Attribute getByAttributeGroup(AttributeGroup attributeGroup) {
		return attributeDao.getByAttributeGroup(attributeGroup);
	}

}
