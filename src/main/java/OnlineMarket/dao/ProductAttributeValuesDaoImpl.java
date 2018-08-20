package OnlineMarket.dao;

import OnlineMarket.model.ProductAttributeId;
import OnlineMarket.model.ProductAttributeValues;
import org.springframework.stereotype.Repository;

@Repository("productAttributeValuesDao")
public class ProductAttributeValuesDaoImpl extends AbstractDao<ProductAttributeId, ProductAttributeValues> implements ProductAttributeValuesDao{

}
