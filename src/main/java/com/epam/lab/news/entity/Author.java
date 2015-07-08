package com.epam.lab.news.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "AUTHORS")
@SequenceGenerator(name = "PK", sequenceName = "AUTHORS_SEQ")
@NamedQuery(name = "authors_by_count_note", query = "select new com.epam.lab.news.model.AuthorCountDto(a.authorId, a.name, count(n)) "
		+ "from Author a inner join a.news n "
		+ "group by a.authorId, a.name  order by count(n) DESC, a.name ")
public class Author implements Serializable {

	private static final long serialVersionUID = -5779389920226231852L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PK")
	@Column(name = "AUTHOR_ID")
	@Min(value = 1, message = "The identifier must be not less than {value}")
	private Long authorId;

	@Column(name = "NAME", nullable = false)
	@NotBlank(message = "Author name should not be empty")
	@Size(min = 1, max = 100, message = "The length of the author name must be {min} to {max}")
	private String name;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "NEWS_AUTHORS", joinColumns = { @JoinColumn(name = "AUTHOR_ID") }, inverseJoinColumns = { @JoinColumn(name = "NEWS_ID") }, uniqueConstraints = { @UniqueConstraint(columnNames = {
			"NEWS_ID", "AUTHOR_ID" }) })
	private Set<News> news;

	public Author() {

	}

	public Author(Long authorId) {
		this.authorId = authorId;
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

	public Set<News> getNews() {
		return news;
	}

	public void setNews(Set<News> news) {
		this.news = news;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(7, 71).append(authorId).append(name)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Author other = (Author) obj;
		return new EqualsBuilder().append(authorId, other.authorId)
				.append(name, other.name).isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("authorId", authorId)
				.append("name", name).toString();
	}
}
