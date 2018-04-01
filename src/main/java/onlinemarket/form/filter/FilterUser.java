package onlinemarket.form.filter;

public class FilterUser {
	
	private FilterForm filterForm;
	
	private Integer role;
	
	private String state;

	public FilterForm getFilterForm() {
		return filterForm;
	}

	public void setFilterForm(FilterForm filterForm) {
		this.filterForm = filterForm;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}
