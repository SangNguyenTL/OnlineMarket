package OnlineMarket.service;

import java.util.List;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.Attribute;
import OnlineMarket.model.AttributeGroup;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.exception.attribute.AttributeHasAttributeValuesException;
import OnlineMarket.util.exception.attribute.AttributeNotFoundException;
import OnlineMarket.util.exception.attributeGroup.AttributeGroupNotFoundException;

public interface AttributeService{

	Attribute getByKey(Integer id);

	void save(Attribute attribute, AttributeGroup attributeGroup) throws AttributeGroupNotFoundException;

	void delete(Integer id) throws AttributeNotFoundException, AttributeHasAttributeValuesException;

	void update(Attribute attribute, AttributeGroup attributeGroup) throws AttributeNotFoundException, AttributeGroupNotFoundException;

	ResultObject<Attribute> listByAttributeGroup(AttributeGroup attributeGroup, FilterForm filterForm) throws AttributeGroupNotFoundException;

	public List<Attribute> listByAttributeGroup(AttributeGroup attributeGroup);
	
}
