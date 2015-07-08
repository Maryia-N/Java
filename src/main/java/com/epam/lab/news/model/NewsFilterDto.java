package com.epam.lab.news.model;

import java.util.HashMap;

import com.epam.lab.news.util.SortField;

public class NewsFilterDto {

	private HashMap<SortField, Object> searchData;
	private Integer commentTop;
	private Integer pageIndex;
	private Integer pageSize;

	public NewsFilterDto() {
	}

	public NewsFilterDto(HashMap<SortField, Object> searchData) {
		this.searchData = searchData;
	}

	public HashMap<SortField, Object> getSearchData() {
		return searchData;
	}

	public void setSearchData(HashMap<SortField, Object> searchData) {
		this.searchData = searchData;
	}

	public Integer getCommentTop() {
		return commentTop;
	}

	public void setCommentTop(Integer commentTop) {
		this.commentTop = commentTop;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
