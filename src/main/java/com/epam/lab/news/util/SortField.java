package com.epam.lab.news.util;

public enum SortField {

	NEWS_ID("newsId"), NEWS_TITLE("title"), NEWS_SHORT_TEXT("shortText"), NEWS_FULL_TEXT(
			"fullText"), NEWS_CREATION_DATE("creationDate"), NEWS_MODIFICATION_DATE(
			"modificationDate"), NEWS_COMMENTS("comments"), NEWS_TAGS("tags"), NEWS_AUTHORS(
			"authors"), COMMENT_ID("commentId");

	private String field;

	SortField(String field) {
		this.field = field;
	}

	public String getField() {
		return field;
	}

}
