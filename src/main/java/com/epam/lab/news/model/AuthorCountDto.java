package com.epam.lab.news.model;

public class AuthorCountDto {

	private Long authorId;

	private String name;

	private long count;

	public AuthorCountDto() {
	}

	public AuthorCountDto(Long authorId, String name, long count) {
		this.authorId = authorId;
		this.name = name;
		this.count = count;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
