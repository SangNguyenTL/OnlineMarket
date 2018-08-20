package OnlineMarket.dao;

import OnlineMarket.model.Attribute;
import OnlineMarket.model.AttributeValues;

public interface AttributeValuesDao extends InterfaceDao<Integer, AttributeValues> {
    AttributeValues getByAttributeAndValue(Attribute attribute, String value);
}
