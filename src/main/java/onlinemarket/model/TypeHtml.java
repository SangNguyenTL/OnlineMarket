package onlinemarket.model;

public enum TypeHtml {
	TEXT("text"),
	URL("url"),
	NUMBER("number"),
	HIDDEN("hidden"),
	TEXTAREA("textarea"),
	EMAIL("email"),
	TEL("tel"),
	DATE("date");
	
	private String name;
	
	private TypeHtml(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
