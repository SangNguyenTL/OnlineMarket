package onlinemarket.util.exception.attributeValues;

public class AttributeValuesHasProductException extends Exception{
    public AttributeValuesHasProductException() {
        super("Attribute value has product.");
    }

    public AttributeValuesHasProductException(String message) {
        super(message);
    }
}
