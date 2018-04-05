package onlinemarket.dao;

import onlinemarket.model.AttributeValues;
import org.springframework.stereotype.Repository;

@Repository("attributeValuesDao")
public class AttributeValuesDaoImpl extends AbstractDao<Integer, AttributeValues> implements AttributeValuesDao {
}
