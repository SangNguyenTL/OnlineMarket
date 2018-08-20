package OnlineMarket.service;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Attribute;
import OnlineMarket.model.AttributeValues;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.exception.attribute.AttributeNotFoundException;
import OnlineMarket.util.exception.attributeValues.AttributeValuesHasProductException;
import OnlineMarket.util.exception.attributeValues.AttributeValuesIsExistException;
import OnlineMarket.util.exception.attributeValues.AttributeValuesNotFoundException;

public interface AttributeValuesService {

    AttributeValues getByKey(Integer id);

    void save(AttributeValues attributeValues, Attribute attribute) throws AttributeNotFoundException, AttributeValuesIsExistException;

    void update(AttributeValues attributeValues, Attribute attribute) throws AttributeNotFoundException, AttributeValuesNotFoundException;

    void delete(Integer id) throws AttributeValuesNotFoundException, AttributeValuesHasProductException;

    ResultObject<AttributeValues> listByAttribute(Attribute attribute, FilterForm filterForm) throws AttributeNotFoundException;

}
