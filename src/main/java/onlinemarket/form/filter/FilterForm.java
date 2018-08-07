package onlinemarket.form.filter;

import java.util.*;

public class FilterForm {
	
	private Map<String, String> groupSearch = new TreeMap<>();

	private List<String> excludeProperties = new ArrayList<>();

	private TreeMap<String, TreeMap<String, Object>> where = new TreeMap<>();

	private String search = "";
	
	private String searchBy = "name";
	
	private String order = "asc";
	
	private String orderBy = "id";
	
	private Integer size = 10;
	
	private int currentPage = 1;

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

	public TreeMap<String, TreeMap<String, Object>> getWhere() {
		return where;
	}

	public void setWhere(TreeMap<String, TreeMap<String, Object>> where) {
		this.where = where;
	}
}
