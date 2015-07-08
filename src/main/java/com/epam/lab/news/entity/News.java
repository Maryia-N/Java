package com.epam.lab.news.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Entity {@code News} represents a table in a relational database
 *
 * @author Maryia_Nabzdorava
 */

@Entity
@Table(name = "NEWS")
@SequenceGenerator(name = "PK", sequenceName = "NEWS_SEQ")
public class News implements Serializable {

	private static final long serialVersionUID = -5141228505693741370L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PK")
	@Column(name = "NEWS_ID")
	@Min(value = 1, message = "The identifier news must be not less than {value}")
	private Long newsId;

	@Column(name = "TITLE")
	@NotBlank(message = "Title name should not be empty")
	@Size(min = 1, max = 30, message = "The length of the title must be {min} to {max}")
	private String title;

	@Column(name = "SHORT_TEXT")
	@NotBlank(message = "Short text name should not be empty")
	@Size(min = 1, max = 100, message = "The length of the short text must be {min} to {max}")
	private String shortText;

	@Column(name = "FULL_TEXT")
	@NotBlank(message = "Full text name should not be empty")
	@Size(min = 1, max = 2000, message = "The length of the full text must be from {min} to {max}")
	private String fullText;

	@CreationTimestamp
	@Column(name = "CREATION_DATE", updatable = false, nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date creationDate;

	@Column(name = "MODIFICATION_DATE", nullable = false)
	@UpdateTimestamp
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date modificationDate;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "NEWS_TAG", joinColumns = { @JoinColumn(name = "NEWS_ID") }, inverseJoinColumns = { @JoinColumn(name = "TAG") }, uniqueConstraints = { @UniqueConstraint(columnNames = {
			"NEWS_ID", "TAG" }) })
	@OrderBy("tagName")
	private Set<Tag> tags;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "NEWS_AUTHORS", joinColumns = { @JoinColumn(name = "NEWS_ID") }, inverseJoinColumns = { @JoinColumn(name = "AUTHOR_ID") }, uniqueConstraints = { @UniqueConstraint(columnNames = {
			"NEWS_ID", "AUTHOR_ID" }) })
	@OrderBy("name")
	private Set<Author> authors;

	@OneToMany(mappedBy = "newsId", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	@OrderBy("creationDate DESC")
	private Set<Comment> comments;

	public News() {
	}

	public Long getNewsId() {
		return newsId;
	}

	public void setNewsId(Long newsId) {
		this.newsId = newsId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	public String getFullText() {
		return fullText;
	}

	public void setFullText(String fullText) {
		this.fullText = fullText;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public Set<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 41).append(newsId).append(title)
				.append(shortText).append(fullText).append(creationDate)
				.append(modificationDate).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final News other = (News) obj;
		return new EqualsBuilder().append(newsId, other.newsId)
				.append(title, other.title).append(shortText, other.shortText)
				.append(fullText, other.fullText)
				.append(creationDate, other.creationDate)
				.append(modificationDate, other.modificationDate).isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("newsId", newsId)
				.append("title", title).append("shortText", shortText)
				.append("fullText", fullText)
				.append("creationDate", creationDate)
				.append("modificationDate", modificationDate).toString();
	}

}
