package OnlineMarket.util.other;

public enum CommentState {

    ACTIVE((byte) 0,"Active"),
    INACTIVE((byte) 1, "Inactive");

    private byte id;
    private String state;

    CommentState(byte id, String state) {
        this.id = id;
        this.state = state;
    }

    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
