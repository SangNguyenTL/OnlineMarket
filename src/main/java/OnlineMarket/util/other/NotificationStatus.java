package OnlineMarket.util.other;

public enum NotificationStatus {

    SEEN((byte) 1,"Seen"),
    UNREAD((byte) 0, "Un read");

    private byte id;
    private String state;

    NotificationStatus(byte id, String state) {
        this.id = id;
        this.state = state;
    }

    public byte getId() {
        return id;
    }

    public String getState() {
        return state;
    }
}
