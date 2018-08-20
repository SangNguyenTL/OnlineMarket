package OnlineMarket.util.exception.attributeGroup;

public class AttributeGroupNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public AttributeGroupNotFoundException() {
        super("The attribute group isn't been exist!");
    }
}
