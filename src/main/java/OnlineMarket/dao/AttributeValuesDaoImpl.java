package OnlineMarket.dao;

import OnlineMarket.model.Attribute;
import OnlineMarket.model.AttributeValues;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository("attributeValuesDao")
public class AttributeValuesDaoImpl extends AbstractDao<Integer, AttributeValues> implements AttributeValuesDao {
    @Override
    public AttributeValues getByAttributeAndValue(Attribute attribute, String value) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("attribute", attribute));
        criteria.add(Restrictions.like("value", value));
        return (AttributeValues) criteria.uniqueResult();
    }
}
