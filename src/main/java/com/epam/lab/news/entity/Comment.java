package com.epam.lab.news.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "COMMENTS")
@SequenceGenerator(name = "PK", sequenceName = "COMMENTS_SEQ")
@NamedQuery(name = "comments_by_news_id", query = "select c "
		+ "from Comment c  where newsId= :newsId "
		+ "order by c.creationDate DESC")
public class Comment implements Serializable {

	private static final long serialVersionUID = 1075735801767929964L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PK")
	@Column(name = "COMMENT_ID")
	private Long commentId;

	@Column(name = "COMMENT_TEXT")
	@NotBlank(message = "Comment text should not be empty")
	@Size(min = 1, max = 300, message = "The length of the comment text must be {min} to {max}")
	private String commentText;

	@Column(name = "CREATION_DATE", nullable = false)
	@UpdateTimestamp
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date creationDate;

	@NotNull(message = "News id should not be empty")
	@Column(name = "NEWS_ID")
	private Long newsId;

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Long getNewsId() {
		return newsId;
	}

	public void setNewsId(Long newsId) {
		this.newsId = newsId;
	}

	@Override
	public int hashCode() {
		return Objects
				.hash(this.commentId, this.commentText, this.creationDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Comment other = (Comment) obj;
		return Objects.equals(this.commentId, other.commentId)
				&& Objects.equals(this.commentText, other.commentText)
				&& Objects.equals(this.creationDate, other.creationDate);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("commentId", commentId)
				.append("commentText", commentText)
				.append("creationDate", creationDate).toString();
	}

}
