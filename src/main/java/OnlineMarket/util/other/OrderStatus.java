package OnlineMarket.util.other;

public enum  OrderStatus {

    WAITING((byte) 0, "Waiting"),
    DELIVERING((byte) 1, "Delivering"),
    COMPLETE((byte) 2, "Complete");

    private Byte id;
    private String state;

    OrderStatus(Byte id, String state) {
        this.id = id;
        this.state = state;
    }

    public Byte getId() {
        return id;
    }

    public void setId(Byte id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
