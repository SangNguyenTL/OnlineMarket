package onlinemarket.model;

public enum NumState {
	
    ACTIVE(0,"Active"),
    INACTIVE(1, "Disable");
	
	private Integer id;
	
	private String name;

	private NumState(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.id);
	}

}
