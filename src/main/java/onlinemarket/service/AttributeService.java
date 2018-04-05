package onlinemarket.service;

import java.util.List;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Attribute;
import onlinemarket.model.AttributeGroup;
import onlinemarket.result.ResultObject;
import onlinemarket.util.exception.attribute.AttributeHasAttributeValuesException;
import onlinemarket.util.exception.attribute.AttributeNotFoundException;
import onlinemarket.util.exception.attributeGroup.AttributeGroupNotFoundException;

public interface AttributeService{

	Attribute getByKey(Integer id);

	void save(Attribute attribute, AttributeGroup attributeGroup) throws AttributeGroupNotFoundException;

	void delete(Integer id) throws AttributeNotFoundException, AttributeHasAttributeValuesException;

	void update(Attribute attribute, AttributeGroup attributeGroup) throws AttributeNotFoundException, AttributeGroupNotFoundException;

	ResultObject<Attribute> listByAttributeGroup(AttributeGroup attributeGroup, FilterForm filterForm) throws AttributeGroupNotFoundException;

	public List<Attribute> listByAttributeGroup(AttributeGroup attributeGroup);
	
}
