package onlinemarket.util.exception.attributeValues;

public class AttributeValuesIsExistException extends Exception {

    public AttributeValuesIsExistException() {
        super("Attribute value is exist");
    }

    public AttributeValuesIsExistException(String message) {
        super(message);
    }
}
