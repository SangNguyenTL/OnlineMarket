package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Attribute;
import onlinemarket.model.AttributeValues;
import onlinemarket.result.ResultObject;
import onlinemarket.util.exception.attribute.AttributeNotFoundException;
import onlinemarket.util.exception.attributeValues.AttributeValuesHasProductException;
import onlinemarket.util.exception.attributeValues.AttributeValuesNotFoundException;

public interface AttributeValuesService {

    AttributeValues getByKey(Integer id);

    void save(AttributeValues attributeValues, Attribute attribute) throws AttributeNotFoundException;

    void update(AttributeValues attributeValues, Attribute attribute) throws AttributeNotFoundException, AttributeValuesNotFoundException;

    void delete(Integer id) throws AttributeValuesNotFoundException, AttributeValuesHasProductException;

    ResultObject<AttributeValues> listByAttribute(Attribute attribute, FilterForm filterForm) throws AttributeNotFoundException;

}
