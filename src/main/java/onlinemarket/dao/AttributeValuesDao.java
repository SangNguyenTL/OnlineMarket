package onlinemarket.dao;

import onlinemarket.model.Attribute;
import onlinemarket.model.AttributeValues;

public interface AttributeValuesDao extends InterfaceDao<Integer, AttributeValues> {
    AttributeValues getByAttributeAndValue(Attribute attribute, String value);
}
