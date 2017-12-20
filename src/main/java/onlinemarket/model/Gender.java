package onlinemarket.model;

public enum Gender {
	
	NONE(0,"Chưa xác định"),
	MALE(1,"Nam"),
	FEMALE(2,"Nữ");
	
	private Gender(final int code, final String text) {
		this.code = code;
		this.text = text;
	}
	
	private int code;
	
	private String text;

	public int getCode() {
		return code;
	}

	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.text;
	}
}
