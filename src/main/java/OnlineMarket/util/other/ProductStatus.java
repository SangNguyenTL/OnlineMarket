package OnlineMarket.util.other;

public enum ProductStatus {

	INSTOCK((byte) 0, "In stock"),
	OUTOFSTOCK((byte) 1, "Out of stock"),
	STOPSELLING((byte) 2, "Stop selling"),
    LOCK((byte) 3, "Lock");
	
	private Byte id;
	
	private String name;

	ProductStatus(Byte id, String name) {
		this.id = id;
		this.name = name;
	}

	public Byte getId() {
		return id;
	}

	public void setId(Byte id) {
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
