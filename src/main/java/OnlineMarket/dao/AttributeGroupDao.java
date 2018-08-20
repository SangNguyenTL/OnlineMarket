package OnlineMarket.dao;

import java.util.List;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.AttributeGroup;
import OnlineMarket.model.ProductCategory;
import OnlineMarket.result.ResultObject;

public interface AttributeGroupDao extends InterfaceDao<Integer, AttributeGroup> {

	AttributeGroup getByProductCategory(ProductCategory productCategory);

	ResultObject<AttributeGroup> listByProductCategory(ProductCategory productCategory, FilterForm filterForm);

	List<AttributeGroup> listByProductCategory(ProductCategory productCategory);

}
