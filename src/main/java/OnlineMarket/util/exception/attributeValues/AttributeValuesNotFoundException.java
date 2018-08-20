package OnlineMarket.util.exception.attributeValues;

public class AttributeValuesNotFoundException extends  Exception {

    private static final long serialVersionUID = 1L;

    public AttributeValuesNotFoundException() {
        super("Attribute value not found.");
    }
}
