package OnlineMarket.util.exception.menuGroup;

public class MenuGroupNotFoundException extends Exception{

    private static final long serialVersionUID = 1L;

    public MenuGroupNotFoundException() {
        super("Menu group not found.");
    }
}
