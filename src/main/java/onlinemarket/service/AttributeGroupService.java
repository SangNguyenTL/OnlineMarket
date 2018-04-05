package onlinemarket.service;

import java.util.List;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.AttributeGroup;
import onlinemarket.model.ProductCategory;
import onlinemarket.result.ResultObject;
import onlinemarket.util.exception.attributeGroup.AttributeGroupHasAttributeException;
import onlinemarket.util.exception.attributeGroup.AttributeGroupNotFoundException;
import onlinemarket.util.exception.productCategory.ProductCategoryNotFoundException;

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

    AttributeGroup getByDeclaration(String key, String value);

}
