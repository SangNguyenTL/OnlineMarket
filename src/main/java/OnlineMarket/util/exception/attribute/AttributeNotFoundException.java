package OnlineMarket.util.exception.attribute;

public class AttributeNotFoundException extends Exception{

    private static final long serialVersionUID = 1L;

    public AttributeNotFoundException() {
        super("The attribute isn't been exist!");
    }
}
