package com.epam.lab.news.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "TAG")
@NamedQuery(name = "tags_by_count_note", query = "select new com.epam.lab.news.model.TagCountDto(t.tagName, count(n)) "
		+ "from Tag t inner join t.news n group by t.tagName order by count(n) DESC, t.tagName ")
public class Tag implements Serializable {

	private static final long serialVersionUID = 8388435446543576420L;

	@Id
	@Column(name = "TAG")
	@NotBlank(message = "Tag name should not be empty")
	@Size(min = 1, max = 100, message = "The length of the tag name must be {min} to {max}")
	private String tagName;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "NEWS_TAG", joinColumns = { @JoinColumn(name = "TAG") }, inverseJoinColumns = { @JoinColumn(name = "NEWS_ID") }, uniqueConstraints = { @UniqueConstraint(columnNames = {
			"NEWS_ID", "TAG" }) })
	private Set<News> news;

	public Tag() {
	}

	public Tag(String tagName) {
		this.tagName = tagName;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Set<News> getNews() {
		return news;
	}

	public void setNews(Set<News> news) {
		this.news = news;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(15, 31).append(tagName).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Tag other = (Tag) obj;
		return new EqualsBuilder().append(tagName, other.tagName).isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("tagName", tagName).toString();
	}
}
