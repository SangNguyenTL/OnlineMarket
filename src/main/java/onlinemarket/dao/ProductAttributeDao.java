package onlinemarket.dao;

import onlinemarket.model.Attribute;
import onlinemarket.model.ProductAttribute;
import onlinemarket.model.ProductAttributeId;

public interface ProductAttributeDao extends InterfaceDao<ProductAttributeId, ProductAttribute>{

	ProductAttribute getByAttribute(Attribute attribute);

}
