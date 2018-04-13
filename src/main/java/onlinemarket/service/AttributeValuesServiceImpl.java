package onlinemarket.service;

import onlinemarket.dao.AttributeValuesDao;
import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Attribute;
import onlinemarket.model.AttributeValues;
import onlinemarket.result.ResultObject;
import onlinemarket.util.exception.attribute.AttributeNotFoundException;
import onlinemarket.util.exception.attributeValues.AttributeValuesHasProductException;
import onlinemarket.util.exception.attributeValues.AttributeValuesIsExistException;
import onlinemarket.util.exception.attributeValues.AttributeValuesNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("attributeValuesServiceImpl")
@Transactional
public class AttributeValuesServiceImpl implements AttributeValuesService{

    @Autowired
    AttributeValuesDao attributeValuesDao;

    @Override
    public AttributeValues getByKey(Integer id) {
        return attributeValuesDao.getByKey(id);
    }

    @Override
    public void save(AttributeValues attributeValues, Attribute attribute) throws AttributeNotFoundException, AttributeValuesIsExistException {
        if(attribute == null) throw new AttributeNotFoundException();
        String[] arrayValue = attributeValues.getValue().split(",");
        if(arrayValue.length > 1){
            AttributeValues attributeValues1;
            List<String> valuesExist = new ArrayList<>();
            for(int i = 0; i < arrayValue.length; i++){
                if(i ==0 )
                    attributeValues1 = new AttributeValues(arrayValue[i].trim(), attributeValues.getLink(), attribute);
                else
                    attributeValues1 = new AttributeValues(arrayValue[i].trim(), attribute);
                if(attributeValuesDao.getByAttributeAndValue(attribute, attributeValues1.getValue()) == null)
                    attributeValuesDao.save(attributeValues1);
                else valuesExist.add(attributeValues1.getValue());
            }
            if(valuesExist.size() > 0)
                throw new AttributeValuesIsExistException(StringUtils.join(valuesExist, ", ") + " is exist!");

        }else {
            if(attributeValuesDao.getByAttributeAndValue(attribute, attributeValues.getValue()) != null) throw new AttributeValuesIsExistException();
            attributeValues.setAttribute(attribute);
            attributeValuesDao.save(attributeValues);
        }
    }

    @Override
    public void update(AttributeValues attributeValues, Attribute attribute) throws AttributeNotFoundException, AttributeValuesNotFoundException {
        if(attribute == null) throw new AttributeNotFoundException();
        if(attributeValuesDao.getByKey(attributeValues.getId()) == null) throw new AttributeValuesNotFoundException();
        attributeValues.setAttribute(attribute);
        attributeValuesDao.update(attributeValues);
    }

    @Override
    public void delete(Integer id) throws AttributeValuesNotFoundException, AttributeValuesHasProductException {
        AttributeValues attributeValues = attributeValuesDao.getByKey(id);
        if(attributeValues == null) throw new AttributeValuesNotFoundException();
        if(attributeValuesDao.getUniqueResultBy("productAttributeValues.attributeValues", attributeValues) != null) throw new AttributeValuesHasProductException();
        attributeValuesDao.delete(attributeValues);
    }

    @Override
    public ResultObject<AttributeValues> listByAttribute(Attribute attribute, FilterForm filterForm) throws AttributeNotFoundException {
        if(attribute == null) throw new AttributeNotFoundException();
        return attributeValuesDao.listByDeclaration("attribute", attribute, filterForm);
    }
}
