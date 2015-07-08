package com.epam.lab.news.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.lab.news.dao.AuthorDao;
import com.epam.lab.news.entity.Author;
import com.epam.lab.news.entity.News;
import com.epam.lab.news.model.AuthorCountDto;
import com.epam.lab.news.service.AuthorService;
import com.epam.lab.news.service.NewsService;

@Service
public class AuthorServiceImpl implements AuthorService {

	@Autowired
	private AuthorDao authorDao;

	@Autowired
	private NewsService newsService;

	@Transactional(readOnly = true)
	@Override
	public Author read(Long id, String... fieldsToExclude) {
		Author author = authorDao.read(id);
		if (author != null) {
			boolean addNews = true;
			if (fieldsToExclude != null) {
				for (String field : fieldsToExclude) {
					if ("news".equals(field)) {
						addNews = false;
						break;
					}
				}
			}
			if (addNews) {
				author.getNews().size();
			}
		}
		return author;
	}

	@Override
	public List<Author> readAll() {
		return authorDao.readAll();
	}

	@Override
	public List<Author> readByLimit(int offset, int limit) {
		return authorDao.readByLimit((offset > 0) ? offset : 0,
				(limit > 0) ? limit : 0);
	}

	@Transactional
	@Override
	public Author save(Author entity) {
		deleteNoExistedNews(entity.getNews());
		return authorDao.save(entity);
	}

	@Transactional
	@Override
	public Author update(Author entity) {
		deleteNoExistedNews(entity.getNews());
		return (read(entity.getAuthorId(), "news") != null) ? save(entity)
				: null;
	}

	@Transactional
	@Override
	public void delete(Long id) {
		authorDao.delete(id);
	}

	@Override
	public Long getCommonCount() {
		return authorDao.getCount();
	}

	@Override
	public List<AuthorCountDto> getAuthorCount() {
		return authorDao.getAuthorCount();
	}

	private Set<News> deleteNoExistedNews(Set<News> news) {
		if (news != null) {
			Iterator<News> iterator = news.iterator();
			while (iterator.hasNext()) {
				News i = iterator.next();
				if (newsService.read(i.getNewsId(), "tags", "authors",
						"comments") == null) {
					iterator.remove();
				}
			}
		}
		return news;
	}

}
