package onlinemarket.dao;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.AttributeGroup;
import onlinemarket.model.ProductCategory;
import onlinemarket.result.ResultObject;

public interface AttributeGroupDao extends InterfaceDao<Integer, AttributeGroup> {

	AttributeGroup getByProductCategory(ProductCategory productCategory);

	ResultObject<AttributeGroup> listByProductCategory(ProductCategory productCategory, FilterForm filterForm);

}
