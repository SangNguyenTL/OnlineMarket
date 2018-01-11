package onlinemarket.dao;

import onlinemarket.model.Attribute;
import onlinemarket.model.AttributeGroup;

public interface AttributeDao extends InterfaceDao<Integer, Attribute>{

	Attribute getByAttributeGroup(AttributeGroup attributeGroup);

}
