package OnlineMarket.dao;

import org.springframework.stereotype.Repository;

import OnlineMarket.model.Attribute;

@Repository("attributeDao")
public class AttributeDaoImpl extends AbstractDao<Integer, Attribute> implements AttributeDao{

}
