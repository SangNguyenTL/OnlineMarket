package onlinemarket.dao;

import onlinemarket.model.ProductAttributeId;
import onlinemarket.model.ProductAttributeValues;
import org.springframework.stereotype.Repository;

@Repository("productAttributeValuesDao")
public class ProductAttributeValuesDaoImpl extends AbstractDao<ProductAttributeId, ProductAttributeValues> implements ProductAttributeValuesDao{

}
