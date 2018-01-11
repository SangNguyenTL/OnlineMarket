package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Attribute;
import onlinemarket.model.AttributeGroup;
import onlinemarket.result.ResultObject;

public interface AttributeService extends InterfaceService<Integer, Attribute>{
	
	Attribute getByAttributeGroup(AttributeGroup attributeGroup);

	ResultObject<Attribute> listByAttributeGroup(AttributeGroup attributeGroup, FilterForm filterForm);
	
}
