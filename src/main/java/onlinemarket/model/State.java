package onlinemarket.model;

public enum State {
	 
    ACTIVE("Active","Hoạt động"),
    INACTIVE("Inactive", "Ngưng hoạt động"),
    DELETED("Deleted","Bị xóa"),
    LOCKED("Locked", "Bị khóa");
     
    private String state;
    
    private String text;
     
    private State(final String state, final String text){
        this.state = state;
        this.text = text;
    }
     
    public String getState(){
        return this.state;
    }
 
    @Override
    public String toString(){
        return this.state;
    }
 
 
    public String getName(){
        return this.text;
    }
 
 
}