package cn.i4.iql.http.bean;

public class BaseBean {
	
	private Integer offset;
	
	private Integer limit;
	
	private String startDate;

	private String endDate;

	private String sort;
	
	private String order;
	
	private String search;
	
	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	@Override
	public String toString() {
		return "BaseBean{" +
				"offset=" + offset +
				", limit=" + limit +
				", startDate='" + startDate + '\'' +
				", endDate='" + endDate + '\'' +
				", sort='" + sort + '\'' +
				", order='" + order + '\'' +
				", search='" + search + '\'' +
				'}';
	}
}
