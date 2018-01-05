package onlinemarket.model;

public enum Gender {
	
	NONE(0,"Other"),
	MALE(1,"Male"),
	FEMALE(2,"Female");
	
	private Gender(final Integer code, final String text) {
		this.code = code;
		this.text = text;
	}
	
	private Integer code;
	
	private String text;

	public Integer getCode() {
		return code;
	}

	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.code);
	}
}
