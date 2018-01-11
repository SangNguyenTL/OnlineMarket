package onlinemarket.service;

import onlinemarket.model.Attribute;
import onlinemarket.model.ProductAttribute;
import onlinemarket.model.ProductAttributeId;

public interface ProductAttributeService extends InterfaceService<ProductAttributeId, ProductAttribute>{

	ProductAttribute getByAttribute(Attribute attribute);

}
