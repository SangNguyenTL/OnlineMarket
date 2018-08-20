package OnlineMarket.util.exception.attributeValues;

public class AttributeValuesHasProductException extends Exception{

    private static final long serialVersionUID = 1L;

    public AttributeValuesHasProductException() {
        super("Attribute value has product.");
    }

    public AttributeValuesHasProductException(String message) {
        super(message);
    }
}
