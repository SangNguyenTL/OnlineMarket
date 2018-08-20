package OnlineMarket.util.exception.attributeValues;

public class AttributeValuesIsExistException extends Exception {

    private static final long serialVersionUID = 1L;

    public AttributeValuesIsExistException() {
        super("Attribute value is exist");
    }

    public AttributeValuesIsExistException(String message) {
        super(message);
    }
}
