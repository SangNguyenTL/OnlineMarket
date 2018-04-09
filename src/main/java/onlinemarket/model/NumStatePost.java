package onlinemarket.model;

public enum  NumStatePost {

    ACTIVE(0,"Active"),
    INACTIVE(1,"Inactive"),
    DELETED(2,"Deleted"),
    LOCKED(3,"Locked");

    private Integer id;

    private String state;

    NumStatePost(final Integer id,final String state){
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
