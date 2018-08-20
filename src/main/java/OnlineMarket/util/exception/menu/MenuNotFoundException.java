package OnlineMarket.util.exception.menu;

public class MenuNotFoundException extends Exception{

    private static final long serialVersionUID = 1L;

    public MenuNotFoundException() {
        super("Menu not found.");
    }
}
