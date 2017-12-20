package onlinemarket.model;

public enum RoleType {
    USER("USER","Khách hàng"),
    ADMIN("ADMIN","Quản Trị");

	private RoleType(final String code, final String text) {
		this.code = code;
		this.text = text;
	}
	
    private String text;
    
    private String code;

	public String getText() {
		return text;
	}
	
	public String getCode() {
		return code;
	}

   @Override
	public String toString() {
		return this.code;
	}
}