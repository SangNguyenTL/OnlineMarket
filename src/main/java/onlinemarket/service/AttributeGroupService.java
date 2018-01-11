package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.AttributeGroup;
import onlinemarket.model.ProductCategory;
import onlinemarket.result.ResultObject;

public interface AttributeGroupService extends InterfaceService<Integer, AttributeGroup> {
	
	public AttributeGroup getByProductCategory(ProductCategory productCategory);

	ResultObject<AttributeGroup> listByProductCategory(ProductCategory productCategory, FilterForm filterForm);
	
}
