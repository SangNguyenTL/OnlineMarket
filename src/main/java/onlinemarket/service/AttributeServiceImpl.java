package onlinemarket.service;

import java.util.List;

import onlinemarket.dao.AttributeValuesDao;
import onlinemarket.util.exception.attribute.AttributeHasAttributeValuesException;
import onlinemarket.util.exception.attribute.AttributeNotFoundException;
import onlinemarket.util.exception.attributeGroup.AttributeGroupNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinemarket.dao.AttributeDao;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Attribute;
import onlinemarket.model.AttributeGroup;
import onlinemarket.result.ResultObject;

@Service("attributeService")
@Transactional
public class AttributeServiceImpl implements AttributeService {

    @Autowired
    AttributeDao attributeDao;

    @Autowired
    AttributeValuesDao attributeValuesDao;

    @Override
    public Attribute getByKey(Integer id) {
        return attributeDao.getByKey(id);
    }

    @Override
    public void save(Attribute attribute, AttributeGroup attributeGroup) throws AttributeGroupNotFoundException {
        if(attributeGroup == null) throw new AttributeGroupNotFoundException();
        attribute.setAttributeGroup(attributeGroup);
        attributeDao.save(attribute);
    }

    @Override
    public void delete(Integer id) throws AttributeNotFoundException, AttributeHasAttributeValuesException {
        Attribute attribute = attributeDao.getByKey(id);
        if(attribute == null) throw new AttributeNotFoundException();
        if(attributeValuesDao.getUniqueResultBy("attribute", attribute) != null) throw new AttributeHasAttributeValuesException();
        attributeDao.delete(attribute);
    }

    @Override
    public void update(Attribute attribute, AttributeGroup attributeGroup) throws AttributeNotFoundException, AttributeGroupNotFoundException {

        if(attributeGroup == null) throw new AttributeGroupNotFoundException();
        Attribute attribute1 = attributeDao.getByKey(attribute.getId());
        if(attribute1 == null) throw new AttributeNotFoundException();
        attribute.setAttributeGroup(attributeGroup);
        attributeDao.update(attribute);

    }

    @Override
    public ResultObject<Attribute> listByAttributeGroup(AttributeGroup attributeGroup, FilterForm filterForm) throws AttributeGroupNotFoundException {
        if (attributeGroup == null) throw new AttributeGroupNotFoundException();
        return attributeDao.listByDeclaration("attributeGroup", attributeGroup, filterForm);
    }

    @Override
    public List<Attribute> listByAttributeGroup(AttributeGroup attributeGroup) {
        return attributeDao.listByDeclaration("attributeGroup", attributeGroup);
    }

}
