package OnlineMarket.result;

import java.util.ArrayList;
import java.util.List;

public class ResultObject<T> {
	
	private int totalPages;

	private long count;
	
	private int currentPage;
	
	private List<T> list = new ArrayList<>();

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPage) {
		this.totalPages = totalPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

}
