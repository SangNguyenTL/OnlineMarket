package onlinemarket.api.result;

import java.util.List;

import onlinemarket.model.Image;

public class ResultImage {
	
	long totalRow;
	
	int currentPage;
	
	List<Image> list;

	public long getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(long totalRow) {
		this.totalRow = totalRow;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public List<Image> getList() {
		return list;
	}

	public void setList(List<Image> list) {
		this.list = list;
	}
	
}
