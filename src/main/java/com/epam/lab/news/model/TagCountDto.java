package com.epam.lab.news.model;

public class TagCountDto {

	private String tagName;

	private long count;

	public TagCountDto() {
	}

	public TagCountDto(String tagName, long count) {
		this.tagName = tagName;
		this.count = count;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

}
