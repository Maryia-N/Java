package com.epam.lab.news.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.lab.news.dao.NewsDao;
import com.epam.lab.news.entity.Author;
import com.epam.lab.news.entity.News;
import com.epam.lab.news.entity.Tag;
import com.epam.lab.news.model.NewsFilterDto;
import com.epam.lab.news.service.AuthorService;
import com.epam.lab.news.service.NewsService;
import com.epam.lab.news.service.TagService;
import com.epam.lab.news.util.SortField;

/**
 * The class {@code NewsServiceImpl} contains the implementation of the abstract
 * methods that describes action with {@link News}
 *
 * @author Maryia_Nabzdorava
 */

@Service
public class NewsServiceImpl implements NewsService {

	@Autowired
	private NewsDao newsDao;

	@Autowired
	private AuthorService authorService;

	@Autowired
	private TagService tagService;

	@Transactional(readOnly = true)
	@Override
	public News read(Long id, String... fieldsToExclude) {
		News news = newsDao.read(id);
		if (news != null) {
			boolean addTags = true;
			boolean addAuthors = true;
			boolean addComments = true;
			if (fieldsToExclude != null) {
				for (String field : fieldsToExclude) {
					if ("tags".equals(field)) {
						addTags = false;
					} else if ("authors".equals(field)) {
						addAuthors = false;
					} else if ("comments".equals(field)) {
						addComments = false;
					}
				}
			}
			if (addTags) {
				news.getTags().size();
			}
			if (addAuthors) {
				news.getAuthors().size();
			}
			if (addComments) {
				news.getComments().size();
			}
		}
		return news;
	}

	@Override
	public List<News> readAll() {
		return newsDao.readAll();
	}

	@Override
	public List<News> readByLimit(int offset, int limit) {
		return newsDao.readByLimit((offset > 0) ? offset : 0,
				(limit > 0) ? limit : 0);
	}

	@Transactional
	@Override
	public News save(News entity) {
		deleteNoExistedTags(entity.getTags());
		deleteNoExistedAuthors(entity.getAuthors());
		return newsDao.save(entity);
	}

	@Transactional
	@Override
	public News update(News entity) {
		deleteNoExistedTags(entity.getTags());
		deleteNoExistedAuthors(entity.getAuthors());
		return (read(entity.getNewsId()) != null) ? save(entity) : null;
	}

	@Transactional
	@Override
	public void delete(Long id) {
		newsDao.delete(id);
	}

	@Override
	public Long getCommonCount() {
		return newsDao.getCount();
	}

	@Override
	public List<News> getNewsByCriteria(Integer offset, Integer size,
			Integer commentTop, Long authorId, String tagName) {
		HashMap<SortField, Object> searchData = new HashMap<SortField, Object>();
		if (authorId != null) {
			searchData.put(SortField.NEWS_AUTHORS, new Author(authorId));
		}
		if (tagName != null) {
			searchData.put(SortField.NEWS_TAGS, new Tag(tagName));
		}
		NewsFilterDto filter = new NewsFilterDto(searchData);
		if (commentTop != null && commentTop > 0) {
			filter.setCommentTop(commentTop);
			filter.setPageIndex(0);
			filter.setPageSize(commentTop);
		} else {
			filter.setPageIndex((offset != null && offset > 0) ? offset : 0);
			filter.setPageSize((size != null && size > 0) ? size : 4);
		}
		return newsDao.getNewsByCriteria(filter);
	}

	private Set<Tag> deleteNoExistedTags(Set<Tag> tags) {
		if (tags != null) {
			Iterator<Tag> iterator = tags.iterator();
			while (iterator.hasNext()) {
				Tag tag = iterator.next();
				if (tagService.read(tag.getTagName(), "news") == null) {
					iterator.remove();
				}
			}
		}
		return tags;
	}

	private Set<Author> deleteNoExistedAuthors(Set<Author> authors) {
		if (authors != null) {
			Iterator<Author> iterator = authors.iterator();
			while (iterator.hasNext()) {
				Author author = iterator.next();
				if (authorService.read(author.getAuthorId(), "news") == null) {
					iterator.remove();
				}
			}
		}
		return authors;
	}

}
