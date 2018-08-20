package OnlineMarket.util.exception.rating;

public class RatingNotFoundException extends Exception{

    private static final long serialVersionUID = 1L;

    public RatingNotFoundException() {super("The rating isn't exist.");}
}
