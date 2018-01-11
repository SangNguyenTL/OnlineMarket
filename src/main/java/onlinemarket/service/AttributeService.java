package onlinemarket.service;

import onlinemarket.model.Attribute;
import onlinemarket.model.AttributeGroup;

public interface AttributeService extends InterfaceService<Integer, Attribute>{
	
	Attribute getByAttributeGroup(AttributeGroup attributeGroup);
	
}
