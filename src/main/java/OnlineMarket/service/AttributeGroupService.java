package OnlineMarket.service;

import java.util.List;

import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.model.AttributeGroup;
import OnlineMarket.model.ProductCategory;
import OnlineMarket.result.ResultObject;
import OnlineMarket.util.exception.attributeGroup.AttributeGroupHasAttributeException;
import OnlineMarket.util.exception.attributeGroup.AttributeGroupNotFoundException;
import OnlineMarket.util.exception.productCategory.ProductCategoryNotFoundException;

public interface AttributeGroupService {

    AttributeGroup getByProductCategory(ProductCategory productCategory) throws ProductCategoryNotFoundException;

    ResultObject<AttributeGroup> listByProductCategory(ProductCategory productCategory, FilterForm filterForm) throws ProductCategoryNotFoundException;

    List<AttributeGroup> listByProductCategory(ProductCategory productCategory) throws ProductCategoryNotFoundException;

    void save(AttributeGroup attributeGroup, ProductCategory productCategory) throws ProductCategoryNotFoundException;

    void update(AttributeGroup attributeGroup, ProductCategory productCategory) throws ProductCategoryNotFoundException, AttributeGroupNotFoundException;

    void delete(Integer id) throws AttributeGroupNotFoundException, AttributeGroupHasAttributeException;

    List<AttributeGroup> list(Integer offset, Integer maxResults);

    List<AttributeGroup> list();

    AttributeGroup getByKey(Integer key);

    AttributeGroup getByDeclaration(String key, Object value);

}
