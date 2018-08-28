package OnlineMarket.form.filter;

import java.util.*;

public class FilterForm {
	
	protected Map<String, String> groupSearch = new TreeMap<>();

    protected List<String> excludeProperties = new ArrayList<>();

    protected String search = "";

    protected String searchBy = "";

    protected String order = "asc";

    protected String orderBy = "id";

    protected Integer size = 10;

    protected int currentPage = 1;

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getSearchBy() {
		return searchBy;
	}

	public void setSearchBy(String searchBy) {
		this.searchBy = searchBy;
	}

	public String getOrder() {
		return order != null ? order.toLowerCase() : null;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		size = size != null && size <= 50 ? size : 10;
		this.size = size;
	}

	public Map<String, String> getGroupSearch() {
		return groupSearch;
	}

	public void setGroupSearch(Map<String, String> groupSearch) {
		this.groupSearch = groupSearch;
	}

	public List<String> getExcludeProperties() {
		return excludeProperties;
	}

	public void setExcludeProperties(List<String> excludeProperties) {
		this.excludeProperties = excludeProperties;
	}

}
