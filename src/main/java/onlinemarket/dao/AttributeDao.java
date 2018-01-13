package onlinemarket.dao;

import java.util.List;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.Attribute;
import onlinemarket.model.AttributeGroup;
import onlinemarket.result.ResultObject;

public interface AttributeDao extends InterfaceDao<Integer, Attribute>{

	Attribute getByAttributeGroup(AttributeGroup attributeGroup);

	ResultObject<Attribute> listByAttributeGroup(AttributeGroup attributeGroup, FilterForm filterForm);

	List<Attribute> listByAttributeGroupNoneFilter(AttributeGroup attributeGroup);

}
