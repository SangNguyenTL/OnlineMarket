package OnlineMarket.util.other;

public enum PostStatus {

    ACTIVE(0,"Active"),
    INACTIVE(1,"Locked");

    private Integer id;

    private String state;

    PostStatus(final Integer id, final String state){
        this.id = id;
        this.state = state;
    }

    public String getState(){
        return this.state;
    }

    @Override
    public String toString(){
        return this.name();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
